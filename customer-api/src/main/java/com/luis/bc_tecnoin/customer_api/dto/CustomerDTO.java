package com.luis.bc_tecnoin.customer_api.dto;

public record CustomerDTO(
        Long customerId,
        Long userId,
        String name,
        String email,
        String address,
        String phone,
        String createdAt,
        String updatedAt
) {}