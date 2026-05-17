package com.luis.bc_tecnoin.product_api.dto;

import lombok.Data;

/**
 * DTO representing a product retrieved from FakeStore API.
 * This DTO is used to decouple external API structure from internal usage.
 */
@Data
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private String image;
}