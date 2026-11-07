package br.com.marketflow.estoque.exception;

public class TransicaoStatusInvalidaException extends RuntimeException {

    public TransicaoStatusInvalidaException(String mensagem) {
        super(mensagem);
    }

}
