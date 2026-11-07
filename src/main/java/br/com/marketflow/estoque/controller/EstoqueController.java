package br.com.marketflow.estoque.controller;

import br.com.marketflow.estoque.dto.EntradaEstoqueRequestDTO;
import br.com.marketflow.estoque.dto.EstoqueResponseDTO;
import br.com.marketflow.estoque.dto.MovimentacaoEstoqueResponseDTO;
import br.com.marketflow.estoque.service.EstoqueService;
import br.com.marketflow.infra.dto.PaginaResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Estoque", description = "Saldos, entradas e histórico (admin)")
@RestController
@RequestMapping("/api/v1/estoque/{produtoId}")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @GetMapping
    public EstoqueResponseDTO consultarEstoque(@PathVariable Long produtoId) {
        return estoqueService.consultarEstoque(produtoId);
    }

    @PostMapping("/entradas")
    @ResponseStatus(HttpStatus.CREATED)
    public EstoqueResponseDTO registrarEntrada(@PathVariable Long produtoId, @Valid @RequestBody EntradaEstoqueRequestDTO request) {
        return estoqueService.registrarEntrada(produtoId, request.quantidade());
    }

    @GetMapping("/movimentacoes")
    public PaginaResponseDTO<MovimentacaoEstoqueResponseDTO> listarMovimentacoes(@PathVariable Long produtoId, @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return estoqueService.listarMovimentacoes(produtoId, pageable);
    }

}
