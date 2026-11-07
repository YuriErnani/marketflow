package br.com.marketflow.carrinho.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "itens_carrinho")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, updatable = false)
    private Long usuarioId;

    @Column(name = "produto_id", nullable = false, updatable = false)
    private Long produtoId;

    @Column(nullable = false)
    private int quantidade;

    public ItemCarrinho(Long usuarioId, Long produtoId, int quantidade) {
        this.usuarioId = usuarioId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public void alterarQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}
