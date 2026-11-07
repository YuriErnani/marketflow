package br.com.marketflow.infra.exception;

import br.com.marketflow.carrinho.exception.CarrinhoVazioException;
import br.com.marketflow.carrinho.exception.ItemCarrinhoNaoEncontradoException;
import br.com.marketflow.categoria.exception.CategoriaJaExisteException;
import br.com.marketflow.categoria.exception.CategoriaNaoEncontradaException;
import br.com.marketflow.estoque.exception.EstoqueInsuficienteException;
import br.com.marketflow.estoque.exception.EstoqueNaoEncontradoException;
import br.com.marketflow.estoque.exception.TransicaoStatusInvalidaException;
import br.com.marketflow.pedido.exception.PedidoNaoEncontradoException;
import br.com.marketflow.pedido.exception.ProdutoIndisponivelException;
import br.com.marketflow.produto.exception.ProdutoNaoEncontradoException;
import br.com.marketflow.usuario.exception.AdminNaoPodeSerDesativadoException;
import br.com.marketflow.usuario.exception.CredenciaisInvalidasException;
import br.com.marketflow.usuario.exception.EmailJaCadastradoException;
import br.com.marketflow.usuario.exception.SenhaAtualInvalidaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    public ProblemDetail tratarEmailJaCadastrado(EmailJaCadastradoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("E-mail já cadastrado");

        return problema;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarViolacaoDeIntegridade(DataIntegrityViolationException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "A operação conflita com dados já existentes."
        );
        problema.setTitle("Conflito de Dados");

        return problema;
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail tratarCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problema.setTitle("Falha na Autenticação");

        return problema;
    }

    @ExceptionHandler(SenhaAtualInvalidaException.class)
    public ProblemDetail tratarSenhaAtualInvalida(SenhaAtualInvalidaException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problema.setTitle("Senha Inválida");

        return problema;
    }

    @ExceptionHandler(AdminNaoPodeSerDesativadoException.class)
    public ProblemDetail tratarDesativacaoDeAdmin(AdminNaoPodeSerDesativadoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("Operação Não Permitida");

        return problema;
    }

    @ExceptionHandler(CategoriaNaoEncontradaException.class)
    public ProblemDetail tratarCategoriaNaoEncontrada(CategoriaNaoEncontradaException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Categoria Não Encontrada");

        return problema;
    }

    @ExceptionHandler(CategoriaJaExisteException.class)
    public ProblemDetail tratarCategoriaJaExiste(CategoriaJaExisteException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("CategoriaRepository Já Existe");

        return problema;
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ProblemDetail tratarProdutoNaoEncontrado(ProdutoNaoEncontradoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Produto Não Encontrado");

        return problema;
    }

    @ExceptionHandler(OrdenacaoInvalidaException.class)
    public ProblemDetail tratarOrdenacaoInvalida(OrdenacaoInvalidaException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problema.setTitle("Ordenação Inválida");

        return problema;
    }

    @ExceptionHandler(EstoqueNaoEncontradoException.class)
    public ProblemDetail tratarEstoqueNaoEncontrado(EstoqueNaoEncontradoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Estoque Não Encontrado");

        return problema;
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ProblemDetail tratarEstoqueInsuficiente(EstoqueInsuficienteException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("Estoque Insuficiente");

        return problema;
    }

    @ExceptionHandler(ItemCarrinhoNaoEncontradoException.class)
    public ProblemDetail tratarItemCarrinhoNaoEncontrado(ItemCarrinhoNaoEncontradoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Item Não Encontrado");

        return problema;
    }

    @ExceptionHandler(CarrinhoVazioException.class)
    public ProblemDetail tratarCarrinhoVazio(CarrinhoVazioException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("Carrinho Vazio");

        return problema;
    }

    @ExceptionHandler(ProdutoIndisponivelException.class)
    public ProblemDetail tratarProdutoIndisponivel(ProdutoIndisponivelException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("Produto Indisponível");

        return problema;
    }

    @ExceptionHandler(PedidoNaoEncontradoException.class)
    public ProblemDetail tratarPedidoNaoEncontrado(PedidoNaoEncontradoException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
        problema.setTitle("Pedido Não Encontrado");

        return problema;
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail tratarConflitoDeConcorrencia(OptimisticLockingFailureException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Os dados foram alterados por outra requisição. Tente novamente.");
        problema.setTitle("Conflito de Concorrência");

        return problema;
    }

    @ExceptionHandler(TransicaoStatusInvalidaException.class)
    public ProblemDetail tratarTransicaoStatusInvalida(TransicaoStatusInvalidaException ex) {
        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage());
        problema.setTitle("Transição de Status Inválida");

        return problema;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> erros = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(erro -> erros.putIfAbsent(erro.getField(), erro.getDefaultMessage()));

        var problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos."
        );
        problema.setTitle("Dados Inválidos");
        problema.setProperty("erros", erros);

        return handleExceptionInternal(ex, problema, headers, status, request);
    }

}
