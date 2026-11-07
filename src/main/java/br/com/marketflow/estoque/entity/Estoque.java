package br.com.marketflow.estoque.entity;

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

}
