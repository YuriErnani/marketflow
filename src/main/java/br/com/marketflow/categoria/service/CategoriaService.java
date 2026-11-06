package br.com.marketflow.categoria.service;

import br.com.marketflow.categoria.dto.CategoriaRequestDTO;
import br.com.marketflow.categoria.dto.CategoriaResponseDTO;
import br.com.marketflow.categoria.entity.Categoria;
import br.com.marketflow.categoria.exception.CategoriaJaExisteException;
import br.com.marketflow.categoria.exception.CategoriaNaoEncontradaException;
import br.com.marketflow.categoria.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository
                .findAll(Sort.by("nome"))
                .stream()
                .map(CategoriaResponseDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarCategoriaPorId(Long id) {
        return CategoriaResponseDTO
                .from(buscarCategoria(id));
    }

    @Transactional
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO request) {
        var nome = request.nome().trim();

        if (categoriaRepository.existsByNomeIgnoreCase(nome))
            throw new CategoriaJaExisteException();

        var categoria = new Categoria(nome, request.descricao());

        return CategoriaResponseDTO
                .from(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponseDTO atualizarCategoria(Long id, CategoriaRequestDTO request) {
        var categoria = buscarCategoria(id);
        var nome = request.nome().trim();

        if (categoriaRepository.existsByNomeIgnoreCaseAndIdNot(nome, id))
            throw new CategoriaJaExisteException();

        categoria.atualizar(nome, request.descricao());

        return CategoriaResponseDTO
                .from(categoria);
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNaoEncontradaException());
    }

}
