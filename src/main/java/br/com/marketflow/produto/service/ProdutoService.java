package br.com.marketflow.produto.service;

import br.com.marketflow.categoria.service.CategoriaService;
import br.com.marketflow.produto.dto.ProdutoRequestDTO;
import br.com.marketflow.produto.dto.ProdutoResponseDTO;
import br.com.marketflow.produto.entity.Produto;
import br.com.marketflow.produto.exception.ProdutoNaoEncontradoException;
import br.com.marketflow.produto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarProduto(Long id, boolean incluirInativos) {
        var produto = buscarProdutoPorId(id);

        if (!produto.isAtivo() && !incluirInativos)
            throw new ProdutoNaoEncontradoException();

        return ProdutoResponseDTO.from(produto);
    }

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoRequestDTO request) {
        var categoria = categoriaService.buscarEntidadePorId(request.categoriaId());

        var produto = new Produto(
                request.nome().trim(),
                request.descricao(),
                request.preco(),
                categoria);

        return ProdutoResponseDTO.from(produtoRepository.save(produto));
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

        return ProdutoResponseDTO.from(produto);
    }

    private Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(ProdutoNaoEncontradoException::new);
    }

}
