package br.com.marketflow.categoria.exception;

public class CategoriaNaoEncontradaException extends RuntimeException {

    public CategoriaNaoEncontradaException() {
        super("CategoriaRepository não encontrada.");
    }

}
