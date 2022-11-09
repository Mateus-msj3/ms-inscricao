package com.io.github.msj.msinscricao.service.implemetation;

import com.io.github.msj.msinscricao.dto.request.InscricaoFinalizacaoRequestDTO;
import com.io.github.msj.msinscricao.dto.request.InscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoFinalizadaResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoResponseDTO;
import com.io.github.msj.msinscricao.enums.Situacao;
import com.io.github.msj.msinscricao.exception.NegocioException;
import com.io.github.msj.msinscricao.infra.mqueue.FinalizarInscricaoPublisher;
import com.io.github.msj.msinscricao.model.Inscricao;
import com.io.github.msj.msinscricao.repository.InscricaoRepository;
import com.io.github.msj.msinscricao.service.CursoClientService;
import com.io.github.msj.msinscricao.service.InscricaoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Autowired
    private FinalizarInscricaoPublisher finalizarInscricaoPublisher;


    @Override
    @Transactional
    public InscricaoMensagemResponseDTO salvar(InscricaoRequestDTO inscricaoRequestDTO) {
        Inscricao inscricaoRequest = modelMapper.map(inscricaoRequestDTO, Inscricao.class);
        inscricaoRepository.save(inscricaoRequest);
        return new InscricaoMensagemResponseDTO("Inscrição realizada com sucesso.");
    }

    @Override
    public InscricaoMensagemResponseDTO finalizar(InscricaoFinalizacaoRequestDTO inscricaoFinalizacaoRequestDTO) {
        try {
            finalizarInscricaoPublisher.finalizarInscricao(inscricaoFinalizacaoRequestDTO);
            return new InscricaoMensagemResponseDTO("Inscrição finalizada com sucesso.");

        }catch (Exception e) {
            throw new NegocioException(e.getMessage());
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

}
