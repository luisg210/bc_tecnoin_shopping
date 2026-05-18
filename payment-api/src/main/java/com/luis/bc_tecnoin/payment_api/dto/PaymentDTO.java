package com.luis.bc_tecnoin.payment_api.dto;

import lombok.Data;

/**
 * DTO used for returning payment information.
 */
@Data
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private Double amount;
    private String status;
    private String createdAt;
}