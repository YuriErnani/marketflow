package br.com.marketflow.estoque.exception;

public class EstoqueNaoEncontradoException extends RuntimeException {

    public EstoqueNaoEncontradoException() {
        super("Estoque não encontrado para este produto.");
    }

}
