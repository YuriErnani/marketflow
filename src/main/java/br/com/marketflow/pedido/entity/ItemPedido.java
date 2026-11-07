package br.com.marketflow.pedido.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, updatable = false)
    private Pedido pedido;

    @Column(name = "produto_id", nullable = false, updatable = false)
    private Long produtoId;

    @Column(name = "nome_produto", nullable = false, length = 120, updatable = false)
    private String nomeProduto;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2, updatable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false, updatable = false)
    private int quantidade;

    @Column(nullable = false, precision = 12, scale = 2, updatable = false)
    private BigDecimal subtotal;

    ItemPedido(Pedido pedido, Long produtoId, String nomeProduto, BigDecimal precoUnitario, int quantidade) {
        this.pedido = pedido;
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

}
