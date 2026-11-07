package br.com.marketflow.pedido.controller;

import br.com.marketflow.infra.dto.PaginaResponseDTO;
import br.com.marketflow.pedido.dto.FinalizarPedidoRequestDTO;
import br.com.marketflow.pedido.dto.PedidoResponseDTO;
import br.com.marketflow.pedido.dto.PedidoResumoResponseDTO;
import br.com.marketflow.pedido.service.PedidoService;
import br.com.marketflow.usuario.dto.UsuarioAuthRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public PaginaResponseDTO<PedidoResumoResponseDTO> listarPedidos(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PageableDefault(size = 10) Pageable pageable) {
        return pedidoService.listarPedidos(usuario.id(), usuario.ehAdmin(), pageable);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPedidoPorId(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PathVariable Long id) {
        return pedidoService.buscarPedidoPorId(id, usuario.id(), usuario.ehAdmin());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO finalizarCompra(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @Valid @RequestBody FinalizarPedidoRequestDTO request) {
        return pedidoService.finalizarCompra(usuario.id(), request);
    }

}