package com.luis.bc_tecnoin.detail_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO used for creating a new order.
 */
@Data
public class CreateOrderDetailDTO {
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;
}