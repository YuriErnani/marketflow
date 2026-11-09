package br.com.marketflow.pedido.exception;

public class TransicaoStatusInvalidaException extends RuntimeException {

    public TransicaoStatusInvalidaException(String mensagem) {
        super(mensagem);
    }

}
