package com.luis.bc_tecnoin.detail_api.controller;

import com.luis.bc_tecnoin.detail_api.dto.error.FieldErrorDTO;
import com.luis.bc_tecnoin.detail_api.dto.error.ValidationErrorDTO;
import com.luis.bc_tecnoin.detail_api.exception.InvalidOrderException;
import com.luis.bc_tecnoin.detail_api.exception.OrderDetailNotFoundException;
import com.luis.bc_tecnoin.detail_api.exception.ProductNotFoundException;
import feign.FeignException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
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

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Invalid order")
    public ResponseEntity<Map<String, String>> handleOrderDetailExists(InvalidOrderException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(OrderDetailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Order detail not found")
    public ResponseEntity<Map<String, String>> handleOrderDetailNotFound(OrderDetailNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponse(responseCode = "404", description = "Product not found")
    public ResponseEntity<Map<String, String>> handleProdcutNotFundNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ApiResponse(responseCode = "503", description = "Upstream service unavailable")
    public ResponseEntity<Map<String, String>> handleFeignException(FeignException ex) {
        log.error("Feign error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "Servicio no disponible"));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
    }

}
