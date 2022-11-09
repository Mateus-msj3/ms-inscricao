package com.io.github.msj.msinscricao.enums;

public enum SituacaoInscricaoCurso {

    EM_ANDAMENTO("Em andamento"),
    FINALIZADO("Finalizado");

    private String situacao;

    SituacaoInscricaoCurso(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return situacao;
    }
}
