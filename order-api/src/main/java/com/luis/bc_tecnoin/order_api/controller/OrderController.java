package com.luis.bc_tecnoin.order_api.controller;

import com.luis.bc_tecnoin.order_api.dto.CreateOrderDTO;
import com.luis.bc_tecnoin.order_api.dto.OrderDTO;
import com.luis.bc_tecnoin.order_api.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints for order management")
public class OrderController {

    private final IOrderService service;

    @PostMapping
    @Operation(summary = "Create order", description = "Creates a new order for a customer")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<OrderDTO> createOrder(@RequestHeader("X-User-Id") Long userId,
                                                @RequestBody CreateOrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(dto, userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Returns an order by its ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get orders by customer", description = "Returns a paginated list of orders for a customer")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    public ResponseEntity<Page<OrderDTO>> getAllOrdersByCustomer(@PathVariable Long customerId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.getAllOrders(customerId, pageable));
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels an existing order by ID")
    @ApiResponse(responseCode = "204", description = "Order cancelled successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        service.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/finish")
    @Operation(summary = "Finish order", description = "Marks an order as finished by ID")
    @ApiResponse(responseCode = "204", description = "Order finished successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<Void> finishOrder(@PathVariable Long id) {
        service.finishOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check order exists", description = "Returns whether an order with the given ID exists")
    @ApiResponse(responseCode = "200", description = "Exists check performed successfully")
    public ResponseEntity<Boolean> existsById(@PathVariable Long id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Get order status", description = "Returns the status of an order by ID")
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOrderStatus(id));
    }
}
