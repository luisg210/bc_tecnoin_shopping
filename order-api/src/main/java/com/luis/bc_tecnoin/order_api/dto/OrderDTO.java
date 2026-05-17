package com.luis.bc_tecnoin.order_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDTO {
    private Long id;
    private Long customerId;
    private String status;
    private String createdAt;
    private String updatedAt;
}