package br.com.marketflow.carrinho.controller;

import br.com.marketflow.carrinho.dto.AdicionarItemCarrinhoRequestDTO;
import br.com.marketflow.carrinho.dto.AlterarQuantidadeItemRequestDTO;
import br.com.marketflow.carrinho.dto.CarrinhoResponseDTO;
import br.com.marketflow.carrinho.service.CarrinhoService;
import br.com.marketflow.usuario.dto.UsuarioAuthRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    @GetMapping
    public CarrinhoResponseDTO consultarItens(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario) {
        return carrinhoService.consultarItens(usuario.id());
    }

    @PostMapping("/itens")
    public CarrinhoResponseDTO adicionarItem(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @Valid @RequestBody AdicionarItemCarrinhoRequestDTO request) {
        return carrinhoService.adicionarItem(usuario.id(), request);
    }

    @PutMapping("/itens/{produtoId}")
    public CarrinhoResponseDTO alterarQuantidadeItem(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PathVariable Long produtoId, @Valid @RequestBody AlterarQuantidadeItemRequestDTO request) {
        return carrinhoService.alterarQuantidadeItem(usuario.id(), produtoId, request.quantidade());
    }

    @DeleteMapping("/itens/{produtoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerItem(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PathVariable Long produtoId) {
        carrinhoService.removerItem(usuario.id(), produtoId);
    }

}
