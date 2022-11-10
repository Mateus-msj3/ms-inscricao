package com.io.github.msj.msinscricao.service.implemetation;

import com.io.github.msj.msinscricao.dto.request.InscricaoFinalizacaoRequestDTO;
import com.io.github.msj.msinscricao.dto.request.InscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoFinalizadaResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoResponseDTO;
import com.io.github.msj.msinscricao.dto.response.PessoaResponseDTO;
import com.io.github.msj.msinscricao.enums.Situacao;
import com.io.github.msj.msinscricao.exception.NegocioException;
import com.io.github.msj.msinscricao.infra.mqueue.FinalizarInscricaoPublisher;
import com.io.github.msj.msinscricao.model.Inscricao;
import com.io.github.msj.msinscricao.repository.InscricaoRepository;
import com.io.github.msj.msinscricao.service.CursoClientService;
import com.io.github.msj.msinscricao.service.InscricaoService;
import com.io.github.msj.msinscricao.service.PessoaClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class InscricaoServiceImpl implements InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private CursoClientService cursoClientService;

    @Autowired
    private PessoaClientService pessoaClientService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FinalizarInscricaoPublisher finalizarInscricaoPublisher;


    @Override
    @Transactional
    public InscricaoMensagemResponseDTO salvar(InscricaoRequestDTO inscricaoRequestDTO) {
        if (inscricaoRepository.existsByCpf(inscricaoRequestDTO.getCpf())) {
            throw new NegocioException("O candidato com cpf: " + inscricaoRequestDTO.getCpf() + " já está inscrito no curso.");
        }
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
    public List<InscricaoResponseDTO> listarInscricaoPorCurso(Integer idCurso) {
        List<Inscricao> inscricoesEncontradas = inscricaoRepository.findByIdCurso(idCurso);
        List<InscricaoResponseDTO> inscricaoResponseDTOS = new ArrayList<>();
        InscricaoResponseDTO inscricaoResponseDTO = new InscricaoResponseDTO();
        for (Inscricao inscricao : inscricoesEncontradas) {
            PessoaResponseDTO pessoa = pessoaClientService.buscarPessoaPorCpf(inscricao.getCpf());
            if (pessoa != null) {
                inscricaoResponseDTO.setNomeInscrito(pessoa.getNome());
                inscricaoResponseDTO.setSobrenome(pessoa.getSobrenome());
                inscricaoResponseDTO.setCpf(pessoa.getCpf());
                inscricaoResponseDTO.setNota(inscricao.getNota());
                inscricaoResponseDTOS.add(inscricaoResponseDTO);
            }
        }
        return inscricaoResponseDTOS;
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
