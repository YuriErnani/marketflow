package br.com.marketflow.pagamento.exception;

public class ChaveIdempotenciaEmUsoException extends RuntimeException {

    public ChaveIdempotenciaEmUsoException() {
        super("Esta chave de idempotência já foi usada em outro pedido.");
    }

}
