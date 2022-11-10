package com.io.github.msj.msinscricao.infra;

import com.io.github.msj.msinscricao.dto.response.PessoaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mspessoas", path = "/pessoas")
public interface PessoaResourceClient {

    @GetMapping
    ResponseEntity<List<PessoaResponseDTO>> listarTodos();

    @GetMapping("/cpf")
    ResponseEntity<PessoaResponseDTO> buscarPorCpf(@RequestParam("cpf") String cpf);

}
