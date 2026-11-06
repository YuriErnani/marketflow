package br.com.marketflow.produto.repository;

import br.com.marketflow.produto.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @EntityGraph(attributePaths = "categoria")
    @Query("""
            SELECT p FROM Produto p
            WHERE (:incluirInativos = true OR p.ativo = true)
              AND LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
              AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)
              AND (:precoMin IS NULL OR p.preco >= :precoMin)
              AND (:precoMax IS NULL OR p.preco <= :precoMax)
            """)
    Page<Produto> buscar(
            @Param("nome") String nome,
            @Param("categoriaId") Long categoriaId,
            @Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax,
            @Param("incluirInativos") boolean incluirInativos,
            Pageable pageable);

}
