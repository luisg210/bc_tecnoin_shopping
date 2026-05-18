package com.luis.bc_tecnoin.payment_api.controller;

import com.luis.bc_tecnoin.payment_api.dto.error.FieldErrorDTO;
import com.luis.bc_tecnoin.payment_api.dto.error.ValidationErrorDTO;
import com.luis.bc_tecnoin.payment_api.exception.InvalidPaymentException;
import com.luis.bc_tecnoin.payment_api.exception.PaymentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExeptionController {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<FieldErrorDTO> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();

        ValidationErrorDTO errorDTO = new ValidationErrorDTO("VALIDATION_ERROR", fieldErrors);
        return ResponseEntity.badRequest().body(errorDTO);

    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPaymentException(InvalidPaymentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

}