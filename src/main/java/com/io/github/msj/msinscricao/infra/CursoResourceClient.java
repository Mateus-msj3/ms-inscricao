package com.io.github.msj.msinscricao.infra;

import com.io.github.msj.msinscricao.dto.response.CursoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "mscurso", path = "/cursos")
public interface CursoResourceClient {

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> listarPorId(@PathVariable Long id);

}
