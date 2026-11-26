package com.ambientiza.api.exceptions;

import com.ambientiza.api.dtos.Respostas.ErroResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorErros {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroResponse> api(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> validacao(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(f -> "Campo '" + f.getField() + "' " + f.getDefaultMessage())
                .orElse("Dados inválidos.");
        return ResponseEntity.badRequest().body(new ErroResponse(mensagem));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> corpoInvalido() {
        return ResponseEntity.badRequest().body(new ErroResponse("Corpo da requisição inválido."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> integridade() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse("Operação conflita com dados já existentes."));
    }
}
