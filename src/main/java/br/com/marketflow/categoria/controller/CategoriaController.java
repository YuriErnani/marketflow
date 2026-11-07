package br.com.marketflow.categoria.controller;

import br.com.marketflow.categoria.dto.CategoriaRequestDTO;
import br.com.marketflow.categoria.dto.CategoriaResponseDTO;
import br.com.marketflow.categoria.service.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Categorias", description = "Consulta pública e gestão pelo admin")
@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarCategoriaPorId(@PathVariable Long id) {
        return categoriaService.buscarCategoriaPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO criarCategoria(@Valid @RequestBody CategoriaRequestDTO request) {
        return categoriaService.criarCategoria(request);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO atualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO request) {
        return categoriaService.atualizarCategoria(id, request);
    }

}
