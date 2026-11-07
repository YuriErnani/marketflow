package br.com.marketflow.pedido.entity;

import br.com.marketflow.estoque.exception.TransicaoStatusInvalidaException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "pedidos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false, updatable = false)
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPedido status;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "endereco_entrega", nullable = false, length = 500, updatable = false)
    private String enderecoEntrega;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido(Long usuarioId, String enderecoEntrega) {
        this.usuarioId = usuarioId;
        this.enderecoEntrega = enderecoEntrega;
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
        this.total = BigDecimal.ZERO;
        this.criadoEm = Instant.now();
        this.atualizadoEm = this.criadoEm;
    }

    public void adicionarItem(Long produtoId, String nomeProduto, BigDecimal precoUnitario, int quantidade) {
        var item = new ItemPedido(this, produtoId, nomeProduto, precoUnitario, quantidade);

        this.itens.add(item);
        this.total = this.total.add(item.getSubtotal());
    }

    public List<ItemPedido> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public void cancelarPedido() {
        var podeCancelar = status == StatusPedido.AGUARDANDO_PAGAMENTO || status == StatusPedido.PAGAMENTO_APROVADO || status == StatusPedido.EM_PREPARACAO;

        if (!podeCancelar)
            throw new TransicaoStatusInvalidaException("Pedidos com status " + status + " não podem ser cancelados.");

        this.status = StatusPedido.CANCELADO;
        this.atualizadoEm = Instant.now();
    }

    public void avancarStatus(StatusPedido novoStatus) {
        var proximoStatus = switch (status) {
            case PAGAMENTO_APROVADO -> StatusPedido.EM_PREPARACAO;
            case EM_PREPARACAO -> StatusPedido.ENVIADO;
            case ENVIADO -> StatusPedido.ENTREGUE;
            default -> null;
        };

        if (proximoStatus == null || proximoStatus != novoStatus)
            throw new TransicaoStatusInvalidaException("Não é possível alterar o status de " + status + " para " + novoStatus + ".");

        this.status = novoStatus;
        this.atualizadoEm = Instant.now();
    }

}
