package com.io.github.msj.msinscricao.service;

import com.io.github.msj.msinscricao.dto.response.CursoResponseDTO;
import com.io.github.msj.msinscricao.infra.CursoResourceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoClientService {

    @Autowired
    private CursoResourceClient cursoResourceClient;

    public CursoResponseDTO dadosDoCurso(Integer idCurso) {
        ResponseEntity<CursoResponseDTO> curso = cursoResourceClient.listarPorId(idCurso);

        return CursoResponseDTO.builder()
                .nome(curso.getBody().getNome())
                .numeroVagas(curso.getBody().getNumeroVagas())
                .build();
    }

    public List<CursoResponseDTO> buscarCursos() {
        ResponseEntity<List<CursoResponseDTO>> response = cursoResourceClient.listarTodos();
        return response.getBody();
    }


}
