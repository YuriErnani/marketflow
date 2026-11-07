package br.com.marketflow.produto.service;

import br.com.marketflow.categoria.service.CategoriaService;
import br.com.marketflow.estoque.service.EstoqueService;
import br.com.marketflow.infra.dto.PaginaResponseDTO;
import br.com.marketflow.infra.exception.OrdenacaoInvalidaException;
import br.com.marketflow.produto.dto.ProdutoRequestDTO;
import br.com.marketflow.produto.dto.ProdutoResponseDTO;
import br.com.marketflow.produto.entity.Produto;
import br.com.marketflow.produto.exception.ProdutoNaoEncontradoException;
import br.com.marketflow.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private static final Set<String> CAMPOS_ORDENACAO = Set.of("nome", "preco", "criadoEm");

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;
    private final EstoqueService estoqueService;

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarProduto(Long id, boolean incluirInativos) {
        var produto = buscarProdutoPorId(id);

        if (!produto.isAtivo() && !incluirInativos)
            throw new ProdutoNaoEncontradoException();

        return ProdutoResponseDTO
                .from(produto);
    }

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO request) {
        var categoria = categoriaService.buscarEntidadePorId(request.categoriaId());

        var produto = new Produto(
                request.nome().trim(),
                request.descricao(),
                request.preco(),
                categoria);

        return ProdutoResponseDTO
                .from(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO request) {
        var produto = buscarProdutoPorId(id);
        var categoria = categoriaService.buscarEntidadePorId(request.categoriaId());

        produto.atualizar(
                request.nome().trim(),
                request.descricao(),
                request.preco(),
                categoria);

        return ProdutoResponseDTO.from(produto);
    }

    @Transactional
    public ProdutoResponseDTO ativarProduto(Long id, boolean ativo) {
        var produto = buscarProdutoPorId(id);
        produto.ativar(ativo);

        return ProdutoResponseDTO
                .from(produto);
    }

    @Transactional(readOnly = true)
    public PaginaResponseDTO<ProdutoResponseDTO> listar(
            String nome,
            Long categoriaId,
            BigDecimal precoMin,
            BigDecimal precoMax,
            boolean incluirInativos,
            Pageable pageable) {

        validarOrdenacao(pageable);

        var termo = nome == null ? "" : nome.trim();

        var pagina = produtoRepository
                .buscar(termo, categoriaId, precoMin, precoMax, incluirInativos, pageable)
                .map(ProdutoResponseDTO::from);

        return PaginaResponseDTO
                .from(pagina);
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO request) {
        var categoria = categoriaService.buscarEntidadePorId(request.categoriaId());

        var produto = new Produto(
                request.nome().trim(),
                request.descricao(),
                request.preco(),
                categoria);

        var produtoSalvo = produtoRepository.save(produto);

        estoqueService.criarEstoque(produtoSalvo.getId());

        return ProdutoResponseDTO
                .from(produtoSalvo);
    }

    private Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);
    }

    private void validarOrdenacao(Pageable pageable) {
        pageable.getSort().forEach(ordem -> {
            if (!CAMPOS_ORDENACAO.contains(ordem.getProperty()))
                throw new OrdenacaoInvalidaException(ordem.getProperty());
        });
    }

}
