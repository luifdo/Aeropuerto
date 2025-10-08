package com.pw.aerropuerto.api.error;


import com.pw.aerropuerto.exception.NotFoundException;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex, WebRequest request) {
        var body = ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false), List.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        var violations = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ApiError.FieldViolation(fieldError.getField(), fieldError.getDefaultMessage())).toList();
        var body = ApiError.of(HttpStatus.BAD_REQUEST, "Validation failed", request.getDescription(false), violations);
        return  ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException ex, WebRequest request) {
        var violations = ex.getConstraintViolations().stream()
                .map(constraintViolation -> new ApiError.FieldViolation(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage())).toList();
        var body = ApiError.of(HttpStatus.BAD_REQUEST, "Constraint failed", request.getDescription(false), violations);
        return  ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        var body = ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false), List.of());
        return  ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        var body = ApiError.of(HttpStatus.CONFLICT, ex.getMessage(), request.getDescription(false), List.of());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, WebRequest request) {
        var body = ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request.getDescription(false), List.of());
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
