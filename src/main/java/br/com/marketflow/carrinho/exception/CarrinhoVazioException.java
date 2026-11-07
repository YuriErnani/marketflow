package br.com.marketflow.carrinho.exception;

public class CarrinhoVazioException extends RuntimeException {

    public CarrinhoVazioException() {
        super("O carrinho está vazio.");
    }

}
