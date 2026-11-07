package br.com.marketflow.pedido.repository;

import br.com.marketflow.pedido.entity.Pedido;
import br.com.marketflow.pedido.entity.StatusPedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findByUsuarioId(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = "itens")
    Optional<Pedido> findComItensById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pedido p WHERE p.id = :id")
    Optional<Pedido> buscarParaAtualizacao(@Param("id") Long id);

    @Query("""
        SELECT p.id FROM Pedido p
        WHERE p.status = :status
          AND p.criadoEm < :limite
        ORDER BY p.id
        """)
    List<Long> buscarIdsCriadosAntesDe(@Param("status") StatusPedido status, @Param("limite") Instant limite);

}
