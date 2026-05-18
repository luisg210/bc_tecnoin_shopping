package com.luis.bc_tecnoin.detail_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDetailDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
    private String createdAt;
    private String updatedAt;
}