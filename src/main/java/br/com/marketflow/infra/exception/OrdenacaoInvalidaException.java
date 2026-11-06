package br.com.marketflow.infra.exception;

public class OrdenacaoInvalidaException extends RuntimeException {

    public OrdenacaoInvalidaException(String campo) {
        super("Não é possível ordenar pelo campo: " + campo);
    }

}
