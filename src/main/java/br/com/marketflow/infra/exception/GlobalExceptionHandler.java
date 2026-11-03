package br.com.marketflow.infra.exception;

import br.com.marketflow.usuario.exception.CredenciaisInvalidasException;
import br.com.marketflow.usuario.exception.EmailJaCadastradoException;
import br.com.marketflow.usuario.exception.SenhaAtualInvalidaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ProblemDetail tratarEmailJaCadastrado(EmailJaCadastradoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("E-mail já cadastrado");

        return problema;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarViolacaoDeIntegridade(DataIntegrityViolationException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "A operação conflita com dados já existentes."
        );
        problema.setTitle("Conflito de Dados");

        return problema;
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail tratarCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problema.setTitle("Falha na Autenticação");

        return problema;
    }

    @ExceptionHandler(SenhaAtualInvalidaException.class)
    public ProblemDetail tratarSenhaAtualInvalida(SenhaAtualInvalidaException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problema.setTitle("Senha Inválida");

        return problema;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> erros = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(erro -> erros.putIfAbsent(erro.getField(), erro.getDefaultMessage()));

        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos."
        );
        problema.setTitle("Dados Inválidos");
        problema.setProperty("erros", erros);

        return handleExceptionInternal(ex, problema, headers, status, request);
    }

}
