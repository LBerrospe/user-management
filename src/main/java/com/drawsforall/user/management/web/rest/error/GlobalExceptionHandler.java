package com.drawsforall.user.management.web.rest.error;

import com.drawsforall.user.management.business.exception.UserNotFoundException;
import com.drawsforall.user.management.web.rest.dto.APIErrorResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return response(ex, request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> response(Exception ex, WebRequest request, HttpStatus status) {
        return handleExceptionInternal(ex, new APIErrorResponseDTO(LocalDateTime.now(), status, ex.getMessage()), new HttpHeaders(), status, request);
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

    @ExceptionHandler({
            IOException.class,
            NumberFormatException.class
    })
    protected ResponseEntity<Object> handleInternalServerErrorException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, new APIErrorResponseDTO(LocalDateTime.now(), status, ex.getMessage()), new HttpHeaders(), status, request);
    }
}
