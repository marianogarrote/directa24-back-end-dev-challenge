package com.directa24.main.challenge.infrastructure.web.controller;

import com.directa24.main.challenge.domain.exception.DomainException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DomainException.class)
    public Mono<ResponseEntity<Object>> handleCustomException(DomainException ex, ServerWebExchange exchange) {
        return buildError(
                ex.getCode(),
                "Data Error: " + ex.getMessage(),
                BAD_REQUEST,
                ex,
                exchange
        );
    }

    @ExceptionHandler(value = Exception.class)
    public Mono<ResponseEntity<Object>> handleGenericException(Exception ex, ServerWebExchange exchange) {
        return buildError(
                "INTERNAL_ERROR",
                "Generic Error: " + ex.getMessage(),
                INTERNAL_SERVER_ERROR,
                ex,
                exchange
        );
    }

    private Mono<ResponseEntity<Object>> buildError(
            String code,
            String message,
            HttpStatus status,
            Exception ex,
            ServerWebExchange exchange
    ) {
        ProblemDetail body = createProblemDetail(ex, status, message, code, null, exchange);
        body.setType(URI.create(exchange.getRequest().getPath().value()));
        return createResponseEntity(body, null, status, exchange);
    }
}