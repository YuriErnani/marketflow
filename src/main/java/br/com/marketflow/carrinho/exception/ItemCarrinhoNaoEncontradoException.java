package br.com.marketflow.carrinho.exception;

public class ItemCarrinhoNaoEncontradoException extends RuntimeException {

    public ItemCarrinhoNaoEncontradoException() {
        super("Este produto não está no carrinho.");
    }

}
