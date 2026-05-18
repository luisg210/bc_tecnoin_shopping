package com.luis.bc_tecnoin.payment_api.controller;

import com.luis.bc_tecnoin.payment_api.dto.CreatePaymentDTO;
import com.luis.bc_tecnoin.payment_api.dto.PaymentDTO;
import com.luis.bc_tecnoin.payment_api.service.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing payment endpoints.
 */
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService service;

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(@RequestBody CreatePaymentDTO dto) {
        return ResponseEntity.ok(service.processPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPaymentById(id));
    }
}