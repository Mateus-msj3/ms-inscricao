package com.io.github.msj.msinscricao.infra;

import com.io.github.msj.msinscricao.dto.request.CursoSituacaoInscricaoRequestDTO;
import com.io.github.msj.msinscricao.dto.response.CursoResponseDTO;
import com.io.github.msj.msinscricao.dto.response.CursoSituacaoInscricaoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "mscurso", path = "/cursos")
public interface CursoResourceClient {

    @GetMapping("/{id}")
    ResponseEntity<CursoResponseDTO> listarPorId(@PathVariable Integer id);

    @GetMapping
    ResponseEntity<List<CursoResponseDTO>> listarTodos();

    @PutMapping("/editar/{idCurso}")
    ResponseEntity<CursoSituacaoInscricaoResponseDTO> atualizarSituacaoInscricao(@PathVariable Long idCurso, @RequestBody CursoSituacaoInscricaoRequestDTO cursoSituacaoInscricaoRequestDTO);

}
