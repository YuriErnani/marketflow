package br.com.marketflow.pagamento.repository;

import br.com.marketflow.pagamento.entity.Pagamento;
import br.com.marketflow.pagamento.entity.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByChaveIdempotencia(String chaveIdempotencia);

    Optional<Pagamento> findByPedidoIdAndStatus(Long pedidoId, StatusPagamento status);

    List<Pagamento> findByPedidoIdOrderByIdAsc(Long pedidoId);

}
