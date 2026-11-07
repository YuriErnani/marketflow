package br.com.marketflow.pedido.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PedidoExpiracaoScheduler {

    private final PedidoService pedidoService;
    private final Duration tempoParaExpirar;

    public PedidoExpiracaoScheduler(PedidoService pedidoService, @Value("${api.pedidos.minutos-para-expirar}") long minutosParaExpirar) {
        this.pedidoService = pedidoService;
        this.tempoParaExpirar = Duration.ofMinutes(minutosParaExpirar);
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void expirarPedidosPendentes() {
        var limite = Instant.now().minus(tempoParaExpirar);
        var ids = pedidoService.buscarIdsParaExpirar(limite);

        for (var id : ids) {
            try {
                if (pedidoService.expirar(id))
                    log.info("Pedido {} cancelado por falta de pagamento.", id);
            } catch (RuntimeException e) {
                log.error("Falha ao expirar o pedido {}.", id, e);
            }
        }
    }

}