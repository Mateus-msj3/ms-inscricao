package com.io.github.msj.msinscricao.service.implemetation;

import com.io.github.msj.msinscricao.dto.request.CursoSituacaoInscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.request.InscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoFinalizadaResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoResponseDTO;
import com.io.github.msj.msinscricao.enums.Situacao;
import com.io.github.msj.msinscricao.enums.SituacaoInscricaoCurso;
import com.io.github.msj.msinscricao.model.Inscricao;
import com.io.github.msj.msinscricao.repository.InscricaoRepository;
import com.io.github.msj.msinscricao.service.CursoClientService;
import com.io.github.msj.msinscricao.service.InscricaoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscricaoServiceImpl implements InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private CursoClientService cursoClientService;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public InscricaoMensagemResponseDTO salvar(InscricaoRequestDTO inscricaoRequestDTO) {
        Inscricao inscricaoRequest = modelMapper.map(inscricaoRequestDTO, Inscricao.class);
        inscricaoRepository.save(inscricaoRequest);
        return new InscricaoMensagemResponseDTO("Inscrição realizada com sucesso.");
    }

    @Override
    @Transactional
    public InscricaoMensagemResponseDTO finalizar(Integer idCurso) {
        List<Inscricao> inscricoesEncontradas = inscricaoRepository.findByIdCurso(idCurso);

        var curso = cursoClientService.dadosDoCurso(idCurso);

        if (inscricoesEncontradas.size() <= curso.getNumeroVagas()) {
            return selecionarInscritos(inscricoesEncontradas, idCurso);
        } else {
            return selecionarInscritosPorNotas(inscricoesEncontradas, curso.getNumeroVagas(), idCurso);
        }
    }

    @Override
    public List<InscricaoResponseDTO> listarPorIdCurso(Integer idCurso) {
        List<Inscricao> inscricoesEncontradas = inscricaoRepository.findByIdCurso(idCurso);
        return inscricoesEncontradas.stream()
                .map(inscricao -> modelMapper.map(inscricao, InscricaoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<InscricaoFinalizadaResponseDTO> inscritosFinalizados(Integer idCurso) {
        List<Inscricao> inscricoesEncontradas = inscricaoRepository.findByIdCurso(idCurso);
        List<InscricaoFinalizadaResponseDTO> retorno = new ArrayList<>();
        for (Inscricao inscricao : inscricoesEncontradas){
            if (inscricao.getSituacao().equals(Situacao.SELECIONADO)) {
                InscricaoFinalizadaResponseDTO inscricaoFinalizadaResponseDTO = modelMapper.map(inscricao, InscricaoFinalizadaResponseDTO.class);
                retorno.add(inscricaoFinalizadaResponseDTO);
            }
        }
        return retorno;
    }

    private InscricaoMensagemResponseDTO selecionarInscritos(List<Inscricao> inscricoes, Integer idCurso) {
        for (Inscricao inscricao : inscricoes) {
            inscricao.setSituacao(Situacao.SELECIONADO);
            inscricaoRepository.save(inscricao);
        }
        atualizarSituacaoInscricaoCurso(idCurso);
        return new InscricaoMensagemResponseDTO("Inscrição finalizada com sucesso.");
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
        return new InscricaoMensagemResponseDTO("Inscrição finalizada com sucesso.");
    }

    private void atualizarSituacaoInscricaoCurso(Integer idCurso) {
        cursoClientService.atualizarSituacaoInscricao(Long.valueOf(idCurso), new CursoSituacaoInscricaoRequestDTO(SituacaoInscricaoCurso.FINALIZADO));
    }
}
