package com.drawsforall.user.management.web.rest.error;

import com.drawsforall.user.management.business.exception.UserNotFoundException;
import com.drawsforall.user.management.web.rest.dto.APIErrorResponseDTO;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        return response(ex, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> response(Exception ex, WebRequest request, HttpStatus status) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), status, request);
    }

    @ExceptionHandler({
            UserNotFoundException.class
    })
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, NOT_FOUND, request);
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    protected ResponseEntity<Object> handleBadRequestException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, BAD_REQUEST, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, new APIErrorResponseDTO(LocalDateTime.now(), status, ex.getMessage()), new HttpHeaders(), status, request);
    }
}
