package com.io.github.msj.msinscricao.service.implemetation;

import com.io.github.msj.msinscricao.dto.request.InscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.CursoResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoFinalizadaResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoResponseDTO;
import com.io.github.msj.msinscricao.enums.Situacao;
import com.io.github.msj.msinscricao.infra.CursoResourceClient;
import com.io.github.msj.msinscricao.model.Inscricao;
import com.io.github.msj.msinscricao.repository.InscricaoRepository;
import com.io.github.msj.msinscricao.service.InscricaoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscricaoServiceImpl implements InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private CursoResourceClient cursoResourceClient;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public InscricaoMensagemResponseDTO salvar(InscricaoRequestDTO inscricaoRequestDTO) {
        Inscricao inscricaoRequest = modelMapper.map(inscricaoRequestDTO, Inscricao.class);
        inscricaoRepository.save(inscricaoRequest);
        return new InscricaoMensagemResponseDTO("Inscrição realizada com sucesso.");
    }

    @Override
    public InscricaoMensagemResponseDTO finalizar(Long idCurso) {
        List<Inscricao> inscricoesEncontradas = inscricaoRepository.findByIdCurso(idCurso);
        //Integer numeroVagasCurso = 1; //cursoService.quantidadeDeVagas();

        var curso = dadosDoCurso(idCurso);

        if (inscricoesEncontradas.size() <= curso.getNumeroVagas()) {
            return selecionarInscritos(inscricoesEncontradas);
        } else {
            return selecionarInscritosPorNotas(inscricoesEncontradas, curso.getNumeroVagas());
        }
    }

    @Override
    public List<InscricaoResponseDTO> listarPorIdCurso(Long idCurso) {
        List<Inscricao> inscricoesEncontradas = inscricaoRepository.findByIdCurso(idCurso);
        return inscricoesEncontradas.stream()
                .map(inscricao -> modelMapper.map(inscricao, InscricaoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<InscricaoFinalizadaResponseDTO> inscritosFinalizados(Long idCurso) {
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

    private CursoResponseDTO dadosDoCurso(Long idCurso) {
        //Buscando o número de vagas no microsservice de curso
        ResponseEntity<CursoResponseDTO> curso = cursoResourceClient.listarPorId(idCurso);

        return CursoResponseDTO.builder()
                .nome(curso.getBody().getNome())
                .numeroVagas(curso.getBody().getNumeroVagas())
                .build();
    }

    private InscricaoMensagemResponseDTO selecionarInscritos(List<Inscricao> inscricoes) {
        for (Inscricao inscricao : inscricoes) {
            inscricao.setSituacao(Situacao.SELECIONADO);
            inscricaoRepository.save(inscricao);
        }
        return new InscricaoMensagemResponseDTO("Inscrição finalizada com sucesso.");
    }

    private InscricaoMensagemResponseDTO selecionarInscritosPorNotas(List<Inscricao> inscricoes, Integer numeroVagas) {
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
        return new InscricaoMensagemResponseDTO("Inscrição finalizada com sucesso.");
    }
}
