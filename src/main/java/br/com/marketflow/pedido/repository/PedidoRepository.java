package br.com.marketflow.pedido.repository;

import br.com.marketflow.pedido.entity.Pedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findByUsuarioId(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = "itens")
    Optional<Pedido> findComItensById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pedido p WHERE p.id = :id")
    Optional<Pedido> buscarParaAtualizacao(@Param("id") Long id);

}
