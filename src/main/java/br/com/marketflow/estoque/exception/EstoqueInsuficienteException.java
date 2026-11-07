package br.com.marketflow.estoque.exception;

public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException() {
        super("Quantidade solicitada indisponível em estoque.");
    }

}
