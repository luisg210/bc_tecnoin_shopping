package com.luis.bc_tecnoin.detail_api.dto;

import lombok.Data;

/**
 * DTO representing product information from external API.
 */
@Data
public class ProductDTO {

    private Long id;
    private String title;
    private Double price;
}