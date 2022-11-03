package com.io.github.msj.msinscricao.handler;

import com.io.github.msj.msinscricao.exception.MensagemErro;
import com.io.github.msj.msinscricao.exception.NegocioException;
import com.io.github.msj.msinscricao.exception.RecursoNaoEncontradoException;
import com.io.github.msj.msinscricao.exception.ValidacaoErro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidacaoErro handleValidationErrors(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<String> messages = bindingResult.getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());
        return new ValidacaoErro(messages);
    }

    @ExceptionHandler(NegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidacaoErro handleRuleBusinessException(NegocioException exception) {
        return new ValidacaoErro(exception);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<MensagemErro> resourceNotFoundException(RecursoNaoEncontradoException ex, WebRequest request) {
        MensagemErro mensagemErro = new MensagemErro(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<MensagemErro>(mensagemErro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensagemErro> globalExceptionHandler(Exception ex, WebRequest request) {
        MensagemErro mensagemErro = new MensagemErro(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<MensagemErro>(mensagemErro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
