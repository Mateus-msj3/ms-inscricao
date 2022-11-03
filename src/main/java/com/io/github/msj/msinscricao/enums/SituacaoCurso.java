package com.io.github.msj.msinscricao.enums;

public enum SituacaoCurso {

    EM_ANDAMENTO("Em andamento"),
    FINALIZADO("Finalizado");

    private String situacao;

    SituacaoCurso(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return situacao;
    }
}
