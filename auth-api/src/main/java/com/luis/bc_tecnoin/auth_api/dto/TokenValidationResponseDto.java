package com.luis.bc_tecnoin.auth_api.dto;


public record TokenValidationResponseDto(
        boolean valid,
        String message
) {}