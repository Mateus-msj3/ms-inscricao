package com.io.github.msj.msinscricao.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PessoaResponseDTO {

    private Long id;

    private String nome;

    private String sobrenome;

    private String cpf;

}
