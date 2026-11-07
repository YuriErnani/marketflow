package br.com.marketflow.estoque.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "movimentacoes_estoque")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "produto_id", nullable = false, updatable = false)
    private Long produtoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, updatable = false)
    private TipoMovimentacao tipo;

    @Column(nullable = false, updatable = false)
    private int quantidade;

    @Column(name = "pedido_id", updatable = false)
    private Long pedidoId;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    public MovimentacaoEstoque(Long produtoId, TipoMovimentacao tipo, int quantidade) {
        this(produtoId, tipo, quantidade, null);
    }

    public MovimentacaoEstoque(Long produtoId, TipoMovimentacao tipo, int quantidade, Long pedidoId) {
        this.produtoId = produtoId;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.pedidoId = pedidoId;
        this.criadoEm = Instant.now();
    }

}
