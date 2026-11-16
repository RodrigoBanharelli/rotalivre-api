package br.com.rotalivre.handler;

import br.com.rotalivre.dto.ErroResponseDto;
import br.com.rotalivre.exception.EntidadeNaoEncontradaException;
import br.com.rotalivre.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

//Tratamento global de excecoes: todo erro volta como ErroResponseDto, sem stack trace e sem SQL.
@RestControllerAdvice
public class ApiExceptionHandler {

    //400 - campos invalidos no corpo (Bean Validation do @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDto> validacoes(MethodArgumentNotValidException e) {
        String detalhes = "";
        for (FieldError field : e.getFieldErrors()) {
            detalhes += field.getField() + ": " + field.getDefaultMessage() + ". ";
        }
        return ResponseEntity.badRequest().body(new ErroResponseDto("Erro 400", detalhes.trim()));
    }

    //400 - JSON mal formatado ou com tipo errado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponseDto> jsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(
                new ErroResponseDto("Erro 400", "JSON invalido ou mal formatado"));
    }

    //400 - codigo na URL nao e numero (ex.: /usuarios/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponseDto> parametroInvalido(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body(
                new ErroResponseDto("Erro 400", "Valor invalido para " + e.getName()));
    }

    //404 - registro nao existe
    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDto> naoEncontrado(EntidadeNaoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponseDto("Erro 404", e.getMessage()));
    }

    //404 - rota nao existe
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponseDto> rotaNaoEncontrada(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErroResponseDto("Erro 404", "Rota nao encontrada"));
    }

    //405 - metodo HTTP nao suportado na rota (ex.: PATCH)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponseDto> metodoNaoSuportado(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                new ErroResponseDto("Erro 405", "Metodo " + e.getMethod() + " nao suportado"));
    }

    //409 - regra de negocio do Rotalivre
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponseDto> regraNegocio(RegraNegocioException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponseDto("Erro 409", e.getMessage()));
    }

    //409 - o banco barrou: registro com filhos (FK) ou valor repetido (UNIQUE)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErroResponseDto> integridade(SQLIntegrityConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErroResponseDto("Erro 409",
                "Operacao nao permitida: o registro possui relatos vinculados ou valor repetido"));
    }

    //500 - falha no banco. Mensagem generica: o SQL nunca vai para o cliente.
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErroResponseDto> erroBanco(SQLException e) {
        e.printStackTrace(); //aparece so no log do servidor
        return ResponseEntity.internalServerError().body(
                new ErroResponseDto("Erro 500", "Procure o administrador do sistema"));
    }

    //500 - qualquer outro erro inesperado, tambem com mensagem generica
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDto> erroInesperado(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(
                new ErroResponseDto("Erro 500", "Procure o administrador do sistema"));
    }
}
