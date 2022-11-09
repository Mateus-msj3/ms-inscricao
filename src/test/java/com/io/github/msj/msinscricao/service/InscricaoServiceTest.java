package com.io.github.msj.msinscricao.service;

import com.io.github.msj.msinscricao.dto.request.CursoSituacaoInscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.request.InscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.CursoResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoFinalizadaResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoResponseDTO;
import com.io.github.msj.msinscricao.enums.Situacao;
import com.io.github.msj.msinscricao.enums.SituacaoInscricaoCurso;
import com.io.github.msj.msinscricao.model.Inscricao;
import com.io.github.msj.msinscricao.repository.InscricaoRepository;
import com.io.github.msj.msinscricao.service.implemetation.InscricaoServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class InscricaoServiceTest {

    @InjectMocks
    InscricaoServiceImpl inscricaoService;

    @Mock
    InscricaoRepository inscricaoRepository;

    @Mock
    CursoClientService cursoClientService;

    @Mock
    ModelMapper modelMapper;

    private final Integer ID = 1;

    @Test
    @DisplayName("Deve salvar uma inscrição")
    public void salvar(){

        var inscricaoRequestDTO = InscricaoRequestDTO.builder().idCurso(1).cpf("11122233344").nota(new BigDecimal(10)).build();
        var inscricao = Inscricao.builder().idCurso(1).cpf("11122233344").nota(new BigDecimal(10)).build();
        var inscricaoSalva = Inscricao.builder().codigo(Long.valueOf(ID)).idCurso(1).cpf("11122233344").nota(new BigDecimal(10)).build();

        when(modelMapper.map(any(), any())).thenReturn(inscricao);
        when(inscricaoRepository.save(inscricao)).thenReturn(inscricaoSalva);
        InscricaoMensagemResponseDTO retorno = inscricaoService.salvar(inscricaoRequestDTO);

        verify(modelMapper, times(1)).map(any(), any());
        verify(inscricaoRepository, times(1)).save(eq(inscricao));

        assertNotNull(retorno, "Verfica se o retorno é diferente de null");
        assertEquals(retorno.getMensagem(), "Inscrição realizada com sucesso.");

    }

    @Test
    @DisplayName("Deve finalizar uma inscricao quando o tamanho da lista estiver igual ou menor ao número de vagas no curso")
    public void finalizar(){

        var inscricao = Inscricao.builder().codigo(Long.valueOf(ID)).cpf("11122233344").idCurso(1).nota(BigDecimal.TEN).build();

        List<Inscricao> inscricoes = Arrays.asList(inscricao);

        when(inscricaoRepository.findByIdCurso(ID)).thenReturn(inscricoes);
        when(cursoClientService.dadosDoCurso(1)).thenReturn(CursoResponseDTO.builder().numeroVagas(1).build());
        when(inscricaoRepository.save(inscricao)).thenReturn(inscricao);
        doNothing().when(cursoClientService).atualizarSituacaoInscricao(1L, new CursoSituacaoInscricaoRequestDTO(SituacaoInscricaoCurso.FINALIZADO));

        InscricaoMensagemResponseDTO retorno = inscricaoService.finalizar(ID);

        verify(inscricaoRepository, times(1)).findByIdCurso(eq(ID));
        verify(inscricaoRepository, times(1)).save(eq(inscricao));

        assertNotNull(retorno, "Verfica se o retorno é diferente de null");
        assertEquals(retorno.getMensagem(), "Inscrição finalizada com sucesso.");

    }

    @Test
    @DisplayName("Deve finalizar uma inscricao quando o tamanho da lista estiver maior que número de vagas no curso, deve selecionar apenas a maior nota")
    public void finalizarComNotasMaiores(){

        var inscricao = Inscricao.builder().codigo(Long.valueOf(ID)).cpf("11122233344").idCurso(1).nota(new BigDecimal(10)).build();
        var inscricao2 = Inscricao.builder().codigo(2L).cpf("22233344411").idCurso(1).nota(new BigDecimal(4)).build();
        var inscricao3 = Inscricao.builder().codigo(3L).cpf("22233344411").idCurso(1).nota(new BigDecimal(7)).build();
        var inscricao4 = Inscricao.builder().codigo(4L).cpf("22233344411").idCurso(1).nota(new BigDecimal(3)).build();
        var inscricao5 = Inscricao.builder().codigo(5L).cpf("22233344411").idCurso(1).nota(new BigDecimal(6)).build();
        var inscricao6 = Inscricao.builder().codigo(6L).cpf("22233344411").idCurso(1).nota(new BigDecimal(5)).build();
        var inscricao7 = Inscricao.builder().codigo(7L).cpf("22233344411").idCurso(1).nota(new BigDecimal(8)).build();
        var inscricao8 = Inscricao.builder().codigo(8L).cpf("22233344411").idCurso(1).nota(new BigDecimal(7)).build();

        List<Inscricao> inscricoes = Arrays.asList(inscricao, inscricao2, inscricao3, inscricao4, inscricao5, inscricao6, inscricao7, inscricao8);

        when(inscricaoRepository.findByIdCurso(ID)).thenReturn(inscricoes);
        when(cursoClientService.dadosDoCurso(1)).thenReturn(CursoResponseDTO.builder().numeroVagas(4).build());
        when(inscricaoRepository.save(inscricao)).thenReturn(inscricao);
        doNothing().when(cursoClientService).atualizarSituacaoInscricao(1L, new CursoSituacaoInscricaoRequestDTO(SituacaoInscricaoCurso.FINALIZADO));

        InscricaoMensagemResponseDTO retorno = inscricaoService.finalizar(ID);

        verify(inscricaoRepository, times(1)).findByIdCurso(eq(ID));
        verify(inscricaoRepository, times(1)).save(eq(inscricao));

        assertNotNull(retorno, "Verfica se o retorno é diferente de null");
        assertEquals(retorno.getMensagem(), "Inscrição finalizada com sucesso.");
    }

    @Test
    @DisplayName("Deve listar os cursos pelo id presente nas inscrições")
    public void listarPorIdCurso(){

        var inscricao = Inscricao.builder().codigo(Long.valueOf(ID)).cpf("11122233344").idCurso(1).nota(new BigDecimal(10)).build();
        var inscricao2 = Inscricao.builder().codigo(2L).cpf("22233344411").idCurso(1).nota(new BigDecimal(4)).build();
        var inscricao3 = Inscricao.builder().codigo(3L).cpf("22233344411").idCurso(1).nota(new BigDecimal(7)).build();

        var retorno1 = InscricaoResponseDTO.builder().cpf(inscricao.getCpf()).nota(inscricao.getNota()).build();
        var retorno2 = InscricaoResponseDTO.builder().cpf(inscricao2.getCpf()).nota(inscricao2.getNota()).build();
        var retorno3 = InscricaoResponseDTO.builder().cpf(inscricao3.getCpf()).nota(inscricao3.getNota()).build();

        List<Inscricao> inscricoes = Arrays.asList(inscricao, inscricao2, inscricao3);
        List<InscricaoResponseDTO> responseDTOS =Arrays.asList(retorno1, retorno2, retorno3);

        when(inscricaoRepository.findByIdCurso(ID)).thenReturn(inscricoes);
        when(modelMapper.map(any(), any())).thenReturn(any());

        List<InscricaoResponseDTO> retorno = inscricaoService.listarPorIdCurso(ID);

        verify(inscricaoRepository, times(1)).findByIdCurso(eq(ID));

        assertNotNull(retorno, "Verfica se o retorno é diferente de null");
    }

    @Test
    @DisplayName("Deve listar os inscritos com situação selecionado")
    public void listarInscritosFinalizados(){

        var inscricao = Inscricao.builder().codigo(Long.valueOf(ID)).cpf("11122233344").idCurso(1).nota(new BigDecimal(10)).situacao(Situacao.SELECIONADO).build();
        var inscricao2 = Inscricao.builder().codigo(2L).cpf("22233344411").idCurso(1).nota(new BigDecimal(4)).situacao(Situacao.NAO_SELECIONADO).build();
        var inscricao3 = Inscricao.builder().codigo(3L).cpf("22233344411").idCurso(1).nota(new BigDecimal(7)).situacao(Situacao.SELECIONADO).build();

        var retorno1 = InscricaoFinalizadaResponseDTO.builder().cpf(inscricao.getCpf()).nota(inscricao.getNota()).situacao(Situacao.SELECIONADO).build();
        var retorno3 = InscricaoFinalizadaResponseDTO.builder().cpf(inscricao3.getCpf()).nota(inscricao3.getNota()).situacao(Situacao.SELECIONADO).build();

        List<Inscricao> inscricoes = Arrays.asList(inscricao, inscricao2, inscricao3);
        List<InscricaoFinalizadaResponseDTO> responseDTOS =Arrays.asList(retorno1, retorno3);

        when(inscricaoRepository.findByIdCurso(ID)).thenReturn(inscricoes);
        when(modelMapper.map(any(), any())).thenReturn(retorno1, retorno3);

        List<InscricaoFinalizadaResponseDTO> retorno = inscricaoService.inscritosFinalizados(ID);

        verify(inscricaoRepository, times(1)).findByIdCurso(eq(ID));

        assertNotNull(retorno, "Verfica se o retorno é diferente de null");
    }
}