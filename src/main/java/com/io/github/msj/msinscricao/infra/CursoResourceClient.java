package com.io.github.msj.msinscricao.infra;

import com.io.github.msj.msinscricao.dto.response.CursoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "mscurso", path = "/cursos")
public interface CursoResourceClient {

    @GetMapping("/{id}")
    ResponseEntity<CursoResponseDTO> listarPorId(@PathVariable Integer id);

    @GetMapping
    ResponseEntity<List<CursoResponseDTO>> listarTodos();

}
