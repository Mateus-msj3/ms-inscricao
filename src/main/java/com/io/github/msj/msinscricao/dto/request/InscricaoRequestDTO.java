package com.io.github.msj.msinscricao.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscricaoRequestDTO {

    private Integer idCurso;

    private Integer cpf;

    private Double nota;

}

