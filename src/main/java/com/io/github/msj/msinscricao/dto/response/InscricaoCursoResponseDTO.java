package com.io.github.msj.msinscricao.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscricaoCursoResponseDTO {

    private Integer cpf;

    private Double nota;

    private String situacao;
}
