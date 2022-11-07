package com.io.github.msj.msinscricao.enums;

public enum Situacao {

    SELECIONADO("Selecionado"),
    NAO_SELECIONADO("Não Selecionado");

    private String situacao;

    Situacao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return situacao;
    }
}
