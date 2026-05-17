package com.luis.bc_tecnoin.product_api.controller;

import com.luis.bc_tecnoin.product_api.dto.ProductDto;
import com.luis.bc_tecnoin.product_api.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST Controller exposing product endpoints.
 * Acts as a proxy to FakeStore API via ProductService.
 */
@RestController
@RequestMapping("/api/v1/products/")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    /**
     * GET /api/v1/products
     * Returns all products from FakeStore API.
     */
    @GetMapping
    public Flux<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * GET /api/v1/products/{id}
     * Returns a single product by ID.
     */
    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}