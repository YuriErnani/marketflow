package br.com.marketflow.usuario.exception;

public class AdminNaoPodeSerDesativadoException extends RuntimeException {

    public AdminNaoPodeSerDesativadoException() {
        super("A conta de administrador não pode ser desativada.");
    }

}
