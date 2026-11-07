package br.com.marketflow.pagamento.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "pagamentos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false, updatable = false)
    private Long pedidoId;

    @Column(name = "chave_idempotencia", nullable = false, unique = true, length = 100, updatable = false)
    private String chaveIdempotencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPagamento status;

    @Column(nullable = false, precision = 12, scale = 2, updatable = false)
    private BigDecimal valor;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    public Pagamento(Long pedidoId, String chaveIdempotencia, BigDecimal valor, StatusPagamento status) {
        this.pedidoId = pedidoId;
        this.chaveIdempotencia = chaveIdempotencia;
        this.valor = valor;
        this.status = status;
        this.criadoEm = Instant.now();
        this.atualizadoEm = this.criadoEm;
    }

    public void estornar() {
        if (status != StatusPagamento.APROVADO)
            throw new IllegalStateException("Só pagamentos aprovados podem ser estornados.");

        this.status = StatusPagamento.ESTORNADO;
        this.atualizadoEm = Instant.now();
    }

}
