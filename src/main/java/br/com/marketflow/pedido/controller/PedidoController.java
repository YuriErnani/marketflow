package br.com.marketflow.pedido.controller;

import br.com.marketflow.infra.dto.PaginaResponseDTO;
import br.com.marketflow.pagamento.dto.PagamentoResponseDTO;
import br.com.marketflow.pagamento.dto.RegistrarPagamentoRequestDTO;
import br.com.marketflow.pedido.dto.AtualizarStatusPedidoRequestDTO;
import br.com.marketflow.pedido.dto.FinalizarPedidoRequestDTO;
import br.com.marketflow.pedido.dto.PedidoResponseDTO;
import br.com.marketflow.pedido.dto.PedidoResumoResponseDTO;
import br.com.marketflow.pedido.service.PedidoService;
import br.com.marketflow.usuario.dto.UsuarioAuthRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Pedidos", description = "Compra, pagamentos, cancelamento e status")
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public PaginaResponseDTO<PedidoResumoResponseDTO> listarPedidos(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return pedidoService.listarPedidos(usuario.id(), usuario.ehAdmin(), pageable);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPedidoPorId(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PathVariable Long id) {
        return pedidoService.buscarPedidoPorId(id, usuario.id(), usuario.ehAdmin());
    }

    @GetMapping("/{id}/pagamentos")
    public List<PagamentoResponseDTO> listarPagamentos(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PathVariable Long id) {
        return pedidoService.listarPagamentos(id, usuario.id(), usuario.ehAdmin());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO finalizarCompra(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @Valid @RequestBody FinalizarPedidoRequestDTO request) {
        return pedidoService.finalizarCompra(usuario.id(), request);
    }

    @PostMapping("/{id}/cancelamento")
    public PedidoResponseDTO cancelar(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PathVariable Long id) {
        return pedidoService.cancelar(id, usuario.id(), usuario.ehAdmin());
    }

    @PostMapping("/{id}/pagamentos")
    public ResponseEntity<PagamentoResponseDTO> registrarPagamento(@AuthenticationPrincipal UsuarioAuthRequestDTO usuario, @PathVariable Long id, @RequestHeader("Idempotency-Key") String chave, @Valid @RequestBody RegistrarPagamentoRequestDTO request) {

        var resultado = pedidoService.registrarPagamento(id, usuario.id(), chave.trim(), request.resultadoSimulado());

        var status = resultado.novo() ? HttpStatus.CREATED : HttpStatus.OK;

        return ResponseEntity
                .status(status)
                .body(resultado.pagamento());
    }

    @PatchMapping("/{id}/status")
    public PedidoResponseDTO atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusPedidoRequestDTO request) {
        return pedidoService.atualizarStatus(id, request.status());
    }

}
