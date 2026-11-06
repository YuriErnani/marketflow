package br.com.marketflow.produto.controller;

import br.com.marketflow.produto.dto.AlterarAtivacaoRequestDTO;
import br.com.marketflow.produto.dto.ProdutoRequestDTO;
import br.com.marketflow.produto.dto.ProdutoResponseDTO;
import br.com.marketflow.produto.service.ProdutoService;
import br.com.marketflow.usuario.dto.UsuarioAuthRequestDTO;
import br.com.marketflow.usuario.entity.Perfil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping("/{id}")
    public ProdutoResponseDTO buscarProdutoPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioAuthRequestDTO usuario) {
        var admin = usuario != null && usuario.perfil() == Perfil.ADMIN;

        return produtoService.buscarProduto(id, admin);
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
