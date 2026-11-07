package br.com.marketflow.estoque.entity;

import br.com.marketflow.estoque.exception.EstoqueInsuficienteException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estoques")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false, unique = true, updatable = false)
    private Long produtoId;

    @Column(name = "quantidade_disponivel", nullable = false)
    private int quantidadeDisponivel;

    @Column(name = "quantidade_reservada", nullable = false)
    private int quantidadeReservada;

    public Estoque(Long produtoId) {
        this.produtoId = produtoId;
        this.quantidadeDisponivel = 0;
        this.quantidadeReservada = 0;
    }

    public void registrarEntrada(int quantidade) {
        this.quantidadeDisponivel += quantidade;
    }

    public void reservar(int quantidade) {
        if (quantidade > this.quantidadeDisponivel)
            throw new EstoqueInsuficienteException();

        this.quantidadeDisponivel -= quantidade;
        this.quantidadeReservada += quantidade;
    }

    public void liberarReserva(int quantidade) {
        if (quantidade > this.quantidadeReservada)
            throw new IllegalStateException("Quantidade reservada insuficiente para liberação.");

        this.quantidadeReservada -= quantidade;
        this.quantidadeDisponivel += quantidade;
    }

    public void devolver(int quantidade) {
        this.quantidadeDisponivel += quantidade;
    }

}
