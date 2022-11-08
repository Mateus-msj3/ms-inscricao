package com.io.github.msj.msinscricao.service;

import com.io.github.msj.msinscricao.dto.response.PessoaResponseDTO;
import com.io.github.msj.msinscricao.infra.PessoaResourceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PessoaClientService {

    @Autowired
    private PessoaResourceClient pessoaResourceClient;

    public List<PessoaResponseDTO> buscarPessoas() {
        ResponseEntity<List<PessoaResponseDTO>> response = pessoaResourceClient.listarTodos();
        return response.getBody();
    }


}
