package br.com.marketflow.categoria.exception;

public class CategoriaJaExisteException extends RuntimeException {

    public CategoriaJaExisteException() {
        super("Já existe uma categoria com este nome.");
    }

}
