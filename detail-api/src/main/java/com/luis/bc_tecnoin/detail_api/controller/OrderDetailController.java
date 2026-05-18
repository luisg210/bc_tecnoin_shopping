package com.luis.bc_tecnoin.detail_api.controller;

import com.luis.bc_tecnoin.detail_api.dto.CreateOrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.service.IOrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller exposing order detail endpoints.
 */
@RestController
@RequestMapping("/api/v1/orders-detail")
@RequiredArgsConstructor
public class OrderDetailController {

    private final IOrderDetailService service;

    @PostMapping
    public ResponseEntity<OrderDetailDTO> createOrderDetail(@Valid @RequestBody CreateOrderDetailDTO dto) {
        return ResponseEntity.ok(service.createOrderDetail(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> getOrderDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderDetailById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<OrderDetailDTO>> getOrderDetailsByOrderId(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getOrderDetailsByOrderId(orderId, pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Long id) {
        service.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }
}