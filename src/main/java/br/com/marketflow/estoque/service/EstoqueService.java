package br.com.marketflow.estoque.service;

import br.com.marketflow.estoque.dto.EstoqueResponseDTO;
import br.com.marketflow.estoque.dto.MovimentacaoEstoqueResponseDTO;
import br.com.marketflow.estoque.entity.Estoque;
import br.com.marketflow.estoque.entity.MovimentacaoEstoque;
import br.com.marketflow.estoque.entity.TipoMovimentacao;
import br.com.marketflow.estoque.exception.EstoqueNaoEncontradoException;
import br.com.marketflow.estoque.repository.EstoqueRepository;
import br.com.marketflow.estoque.repository.MovimentacaoEstoqueRepository;
import br.com.marketflow.infra.dto.PaginaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    @Transactional
    public void criarEstoque(Long produtoId) {
        estoqueRepository.save(new Estoque(produtoId));
    }

    @Transactional(readOnly = true)
    public EstoqueResponseDTO consultar(Long produtoId) {
        var estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(EstoqueNaoEncontradoException::new);

        return EstoqueResponseDTO.from(estoque);
    }

    @Transactional
    public EstoqueResponseDTO registrarEntrada(Long produtoId, int quantidade) {
        var estoque = estoqueRepository.buscarParaAtualizacao(produtoId)
                .orElseThrow(EstoqueNaoEncontradoException::new);

        estoque.registrarEntrada(quantidade);

        movimentacaoRepository.save(new MovimentacaoEstoque(produtoId, TipoMovimentacao.ENTRADA, quantidade));

        return EstoqueResponseDTO.from(estoque);
    }

    @Transactional(readOnly = true)
    public PaginaResponseDTO<MovimentacaoEstoqueResponseDTO> listarMovimentacoes(Long produtoId, Pageable pageable) {
        if (!estoqueRepository.existsByProdutoId(produtoId))
            throw new EstoqueNaoEncontradoException();

        var paginaOrdenada = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "criadoEm"));

        var pagina = movimentacaoRepository
                .findByProdutoId(produtoId, paginaOrdenada)
                .map(MovimentacaoEstoqueResponseDTO::from);

        return PaginaResponseDTO.from(pagina);
    }

    @Transactional(readOnly = true)
    public int quantidadeDisponivel(Long produtoId) {
        return estoqueRepository
                .findByProdutoId(produtoId)
                .map(Estoque::getQuantidadeDisponivel)
                .orElse(0);
    }

    @Transactional(readOnly = true)
    public Map<Long, Integer> quantidadesDisponiveis(Collection<Long> produtoIds) {
        return estoqueRepository
                .findByProdutoIdIn(produtoIds)
                .stream()
                .collect(Collectors.toMap(Estoque::getProdutoId, Estoque::getQuantidadeDisponivel));
    }

}
