package com.luis.bc_tecnoin.payment_api.controller;

import com.luis.bc_tecnoin.payment_api.dto.CreatePaymentDTO;
import com.luis.bc_tecnoin.payment_api.dto.PaymentDTO;
import com.luis.bc_tecnoin.payment_api.service.IPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Endpoints for payment processing")
public class PaymentController {

    private final IPaymentService service;

    @PostMapping
    @Operation(summary = "Process payment", description = "Processes a payment for an order")
    @ApiResponse(responseCode = "201", description = "Payment processed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<PaymentDTO> processPayment(@RequestBody CreatePaymentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.processPayment(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Returns a payment by its ID")
    @ApiResponse(responseCode = "200", description = "Payment found")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPaymentById(id));
    }
}
