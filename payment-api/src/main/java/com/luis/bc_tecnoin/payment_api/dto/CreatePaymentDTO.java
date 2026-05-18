package com.luis.bc_tecnoin.payment_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO used for initiating a payment.
 */
@Data
public class CreatePaymentDTO {
    @NotNull(message = "Order ID is required")
    private Long orderId;
}