package com.io.github.msj.msinscricao.dto.response;

import com.io.github.msj.msinscricao.enums.Situacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscricaoFinalizadaResponseDTO {

    private String cpf;

    private BigDecimal nota;

    private Situacao situacao;

}

