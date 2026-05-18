package com.luis.bc_tecnoin.detail_api.controller;

import com.luis.bc_tecnoin.detail_api.dto.CreateOrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderDetailDTO;
import com.luis.bc_tecnoin.detail_api.dto.OrderWithDetailsDTO;
import com.luis.bc_tecnoin.detail_api.service.IOrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders-detail")
@RequiredArgsConstructor
@Tag(name = "Order Details", description = "Endpoints for order detail management")
public class OrderDetailController {

    private final IOrderDetailService service;

    @PostMapping
    @Operation(summary = "Create order detail", description = "Creates a new order detail entry")
    @ApiResponse(responseCode = "201", description = "Order detail created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<OrderDetailDTO> createOrderDetail(@Valid @RequestBody CreateOrderDetailDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrderDetail(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order detail by ID", description = "Returns an order detail by its ID")
    @ApiResponse(responseCode = "200", description = "Order detail found")
    @ApiResponse(responseCode = "404", description = "Order detail not found")
    public ResponseEntity<OrderDetailDTO> getOrderDetailById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderDetailById(id));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get details by order ID", description = "Returns a paginated list of order details for an order")
    @ApiResponse(responseCode = "200", description = "Order details retrieved successfully")
    public ResponseEntity<Page<OrderDetailDTO>> getOrderDetailsByOrderId(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getOrderDetailsByOrderId(orderId, pageable));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order detail", description = "Deletes an order detail by ID")
    @ApiResponse(responseCode = "204", description = "Order detail deleted successfully")
    @ApiResponse(responseCode = "404", description = "Order detail not found")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable Long id) {
        service.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check order detail exists", description = "Returns whether an order detail with the given ID exists")
    @ApiResponse(responseCode = "200", description = "Exists check performed successfully")
    public ResponseEntity<Boolean> existsOrderDetail(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @GetMapping("/order/{orderId}/total")
    @Operation(summary = "Calculate order total", description = "Calculates the total amount for an order")
    @ApiResponse(responseCode = "200", description = "Total calculated successfully")
    public ResponseEntity<Double> calculateTotal(@PathVariable Long orderId) {
        Double total = service.calculateTotal(orderId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/order/{orderId}/full")
    @Operation(summary = "Get order with all details", description = "Returns the order information along with all its detail lines and the total")
    @ApiResponse(responseCode = "200", description = "Order with details retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderWithDetailsDTO> getOrderWithDetails(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getOrderWithDetails(orderId));
    }
}
