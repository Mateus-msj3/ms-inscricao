package com.io.github.msj.msinscricao.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InscricaoCursoRequestDTO {

    private Integer idCurso;

    private Integer cpf;

    private Double nota;

}

