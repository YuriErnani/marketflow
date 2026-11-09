package br.com.marketflow.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;

public class TamanhoMaximoEmBytesValidator implements ConstraintValidator<TamanhoMaximoEmBytes, String> {

    private int limite;

    @Override
    public void initialize(TamanhoMaximoEmBytes anotacao) {
        this.limite = anotacao.value();
    }

    @Override
    public boolean isValid(String valor, ConstraintValidatorContext contexto) {
        return valor == null || valor.getBytes(StandardCharsets.UTF_8).length <= limite;
    }

}
