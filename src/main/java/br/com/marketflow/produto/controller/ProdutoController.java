package br.com.marketflow.produto.controller;

import br.com.marketflow.infra.dto.PaginaResponseDTO;
import br.com.marketflow.produto.dto.AlterarAtivacaoRequestDTO;
import br.com.marketflow.produto.dto.ProdutoRequestDTO;
import br.com.marketflow.produto.dto.ProdutoResponseDTO;
import br.com.marketflow.produto.service.ProdutoService;
import br.com.marketflow.usuario.dto.UsuarioAuthRequestDTO;
import br.com.marketflow.usuario.entity.Perfil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Produtos", description = "Catálogo com busca, filtros e paginação")
@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public PaginaResponseDTO<ProdutoResponseDTO> listarProdutos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) BigDecimal precoMin,
            @RequestParam(required = false) BigDecimal precoMax,
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            @AuthenticationPrincipal UsuarioAuthRequestDTO usuario) {

        return produtoService.listarProdutos(nome, categoriaId, precoMin, precoMax, ehAdmin(usuario), pageable);
    }

    private boolean ehAdmin(UsuarioAuthRequestDTO usuario) {
        return usuario != null && usuario.perfil() == Perfil.ADMIN;
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarProdutoPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioAuthRequestDTO usuario) {
        return produtoService.buscarProduto(id, ehAdmin(usuario));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO criarProduto(@Valid @RequestBody ProdutoRequestDTO request) {
        return produtoService.criarProduto(request);
    }

    @PutMapping("/{id}")
    public ProdutoResponseDTO atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoRequestDTO request) {
        return produtoService.atualizarProduto(id, request);
    }

    @PatchMapping("/{id}/ativacao")
    public ProdutoResponseDTO ativarProduto(@PathVariable Long id, @Valid @RequestBody AlterarAtivacaoRequestDTO request) {
        return produtoService.ativarProduto(id, request.ativo());
    }

}
