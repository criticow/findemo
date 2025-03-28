package com.waddahex.financeiroapi.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.waddahex.financeiroapi.dto.CustomResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<CustomResponse<Void>> handleGeneralException(Exception ex) {
    CustomResponse<Void> response = new CustomResponse<>();
    response.setMessage("Erro interno do servidor: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<CustomResponse<Void>> handleUnauthorizedAccess(UnauthorizedException ex) {
    CustomResponse<Void> response = new CustomResponse<>();
    response.setMessage("Usuário não autenticado ou token inválido.");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<CustomResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }

    CustomResponse<Void> response = new CustomResponse<>();
    response.setMessage("Erro de validação");
    response.setErrors(errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}