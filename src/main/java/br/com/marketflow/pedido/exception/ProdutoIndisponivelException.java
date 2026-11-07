package br.com.marketflow.pedido.exception;

public class ProdutoIndisponivelException extends RuntimeException {

    public ProdutoIndisponivelException(String nomeProduto) {
        super("O produto " + nomeProduto + " não está disponível para compra.");
    }

}
