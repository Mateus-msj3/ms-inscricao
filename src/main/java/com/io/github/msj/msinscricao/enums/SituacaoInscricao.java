package com.io.github.msj.msinscricao.enums;

public enum SituacaoInscricao {

    SELECIONADO("Selecionado"),
    NAO_SELECIONADO("Não Selecionado");

    private String situacao;

    SituacaoInscricao(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return situacao;
    }
}
