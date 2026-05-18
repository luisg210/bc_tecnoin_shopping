package com.luis.bc_tecnoin.auth_api.controller;

import com.luis.bc_tecnoin.auth_api.dto.error.FieldErrorDTO;
import com.luis.bc_tecnoin.auth_api.dto.error.ValidationErrorDTO;
import com.luis.bc_tecnoin.auth_api.exception.InvalidCredentialsException;
import com.luis.bc_tecnoin.auth_api.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExeptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ApiResponse(responseCode = "400", description = "Validation error")
    public ResponseEntity<ValidationErrorDTO> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();

        ValidationErrorDTO errorDTO = new ValidationErrorDTO("VALIDATION_ERROR", fieldErrors);
        return ResponseEntity.badRequest().body(errorDTO);

    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected error: " + ex.getMessage()));
    }

}
