package br.com.marketflow.pedido.repository;

import br.com.marketflow.pedido.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findByUsuarioId(Long usuarioId, Pageable pageable);

    @EntityGraph(attributePaths = "itens")
    Optional<Pedido> findComItensById(Long id);

}
