package com.io.github.msj.msinscricao.infra.mqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.io.github.msj.msinscricao.dto.request.CursoSituacaoInscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.request.InscricaoFinalizacaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.enums.Situacao;
import com.io.github.msj.msinscricao.enums.SituacaoInscricaoCurso;
import com.io.github.msj.msinscricao.model.Inscricao;
import com.io.github.msj.msinscricao.repository.InscricaoRepository;
import com.io.github.msj.msinscricao.service.CursoClientService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Component
public class FinalizarInscricaoSubscriber {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private CursoClientService cursoClientService;

    @Autowired
    private ModelMapper modelMapper;

    @RabbitListener(queues = "${mq.queues.finalizar-inscricao}")
    public void receberSolicitacaoFinalizarInscricao(@Payload  String payload) {
       var mapper = new ObjectMapper();

       try{
           InscricaoFinalizacaoRequestDTO dto =  mapper.readValue(payload, InscricaoFinalizacaoRequestDTO.class);
           finalizarInscricao(dto);
       }catch (JsonProcessingException e) {
            e.printStackTrace();
       }
    }

    @Transactional
    public InscricaoMensagemResponseDTO finalizarInscricao(InscricaoFinalizacaoRequestDTO inscricaoFinalizacaoRequestDTO) throws JsonProcessingException {

        List<Inscricao> inscricoesEncontradas = inscricaoRepository.findByIdCurso(inscricaoFinalizacaoRequestDTO.getIdCurso());

        var curso = cursoClientService.dadosDoCurso(inscricaoFinalizacaoRequestDTO.getIdCurso());

        if (inscricoesEncontradas.size() <= curso.getNumeroVagas()) {
            return selecionarInscritos(inscricoesEncontradas, inscricaoFinalizacaoRequestDTO.getIdCurso());
        } else {
            return selecionarInscritosPorNotas(inscricoesEncontradas, curso.getNumeroVagas(), inscricaoFinalizacaoRequestDTO.getIdCurso());
        }
    }

    private InscricaoMensagemResponseDTO selecionarInscritos(List<Inscricao> inscricoes, Integer idCurso) {
        for (Inscricao inscricao : inscricoes) {
            inscricao.setSituacao(Situacao.SELECIONADO);
            inscricaoRepository.save(inscricao);
        }
        atualizarSituacaoInscricaoCurso(idCurso);
        return new InscricaoMensagemResponseDTO("Inscritos selecionados com sucesso.");
    }

    private InscricaoMensagemResponseDTO selecionarInscritosPorNotas(List<Inscricao> inscricoes, Integer numeroVagas, Integer idCurso) {
        inscricoes.sort(Comparator.comparing(Inscricao::getNota).reversed());

        for (int i = 0; i < inscricoes.size(); i++) {
            if (i < numeroVagas) {
                inscricoes.get(i).setSituacao(Situacao.SELECIONADO);
                inscricaoRepository.save(inscricoes.get(i));
            } else {
                inscricoes.get(i).setSituacao(Situacao.NAO_SELECIONADO);
                inscricaoRepository.save(inscricoes.get(i));
            }
        }
        atualizarSituacaoInscricaoCurso(idCurso);
        return new InscricaoMensagemResponseDTO("Inscritos selecionados com sucesso.");
    }

    private void atualizarSituacaoInscricaoCurso(Integer idCurso) {
        cursoClientService.atualizarSituacaoInscricao(Long.valueOf(idCurso), new CursoSituacaoInscricaoRequestDTO(SituacaoInscricaoCurso.FINALIZADO));
    }
}
