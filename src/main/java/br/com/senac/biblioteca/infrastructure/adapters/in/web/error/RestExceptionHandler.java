package br.com.senac.biblioteca.infrastructure.adapters.in.web.error;
import br.com.senac.biblioteca.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.time.Instant;
@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BusinessConflictException.class)
    public ProblemDetail handleBusinessConflict(BusinessConflictException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Conflito de Regra de Negocio");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Recurso Nao Encontrado");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Requisicao Invalida");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Campos invalidos na requisicao.");
        problem.setTitle("Erro de Validacao");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno processando a solicitacao.");
        problem.setTitle("Erro Interno");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}