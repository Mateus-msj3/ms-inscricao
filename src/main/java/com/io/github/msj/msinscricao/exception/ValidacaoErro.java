package com.io.github.msj.msinscricao.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public class ValidacaoErro {

    @Getter
    private List<String> errors;

    public ValidacaoErro(List<String> errors) {
        this.errors = errors;
    }

    public ValidacaoErro(String message) {
        this.errors = Arrays.asList(message);
    }

    public ValidacaoErro(NegocioException exception) {
        this.errors = Arrays.asList(exception.getMessage());
    }
}
