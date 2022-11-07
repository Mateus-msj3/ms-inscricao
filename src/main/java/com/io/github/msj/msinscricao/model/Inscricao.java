package com.io.github.msj.msinscricao.model;

import com.io.github.msj.msinscricao.enums.Situacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Inscricao {

    private Integer idCurso;

    private String cpf;

    private Double nota;

    private Situacao situacao;

}
