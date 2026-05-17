package com.luis.bc_tecnoin.order_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO used for creating a new order.
 */
@Data
public class CreateOrderDTO {
    @NotNull(message = "Customer ID is required")
    private Long customerId;
}