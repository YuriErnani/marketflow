package br.com.marketflow.usuario.exception;

public class SenhaAtualInvalidaException extends RuntimeException {

    public SenhaAtualInvalidaException() {
        super("Senha incorreta.");
    }

}
