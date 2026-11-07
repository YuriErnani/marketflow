package br.com.marketflow.carrinho.repository;

import br.com.marketflow.carrinho.entity.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    List<ItemCarrinho> findByUsuarioIdOrderByIdAsc(Long usuarioId);

    Optional<ItemCarrinho> findByUsuarioIdAndProdutoId(Long usuarioId, Long produtoId);

}
