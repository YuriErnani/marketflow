package br.com.marketflow.pagamento.exception;

public class ChaveIdempotenciaInvalidaException extends RuntimeException {

    public ChaveIdempotenciaInvalidaException() {
        super("O cabeçalho Idempotency-Key deve ter entre 1 e 100 caracteres.");
    }

}
