package com.io.github.msj.msinscricao.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MensagemErro {

    private int codigoStatus;

    private Date data;

    private String mensagem;

    private String descricao;
}
