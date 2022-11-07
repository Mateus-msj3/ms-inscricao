package com.io.github.msj.msinscricao.service;

import com.io.github.msj.msinscricao.dto.request.InscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoFinalizadaResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoMensagemResponseDTO;
import com.io.github.msj.msinscricao.dto.response.InscricaoResponseDTO;

import java.util.List;

public interface InscricaoService {

    InscricaoMensagemResponseDTO salvar(InscricaoRequestDTO inscricaoRequestDTO);

    InscricaoMensagemResponseDTO finalizar(Long idCurso);

    List<InscricaoResponseDTO> listarPorIdCurso(Long idCurso);

    List<InscricaoFinalizadaResponseDTO> inscritosFinalizados(Long idCurso);

}
