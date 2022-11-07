package com.io.github.msj.msinscricao.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscricaoRequestDTO {

    private Integer idCurso;

    private String cpf;

    private BigDecimal nota;

}

