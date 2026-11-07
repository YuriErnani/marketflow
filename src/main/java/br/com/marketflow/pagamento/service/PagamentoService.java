package br.com.marketflow.pagamento.service;

import br.com.marketflow.pagamento.dto.PagamentoResponseDTO;
import br.com.marketflow.pagamento.entity.Pagamento;
import br.com.marketflow.pagamento.entity.StatusPagamento;
import br.com.marketflow.pagamento.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    @Transactional(readOnly = true)
    public Optional<Pagamento> buscarPorChave(String chave) {
        return pagamentoRepository.findByChaveIdempotencia(chave);
    }

    @Transactional
    public Pagamento registrar(Long pedidoId, String chave, BigDecimal valor, StatusPagamento status) {
        return pagamentoRepository.save(new Pagamento(pedidoId, chave, valor, status));
    }

    @Transactional
    public void estornarAprovado(Long pedidoId) {
        pagamentoRepository.findByPedidoIdAndStatus(pedidoId, StatusPagamento.APROVADO)
                .ifPresent(Pagamento::estornar);
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> listarPorPedido(Long pedidoId) {
        return pagamentoRepository.findByPedidoIdOrderByIdAsc(pedidoId)
                .stream()
                .map(PagamentoResponseDTO::from)
                .toList();
    }

}
