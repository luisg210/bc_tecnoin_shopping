package com.luis.bc_tecnoin.product_api.controller;

import com.luis.bc_tecnoin.product_api.dto.ProductDto;
import com.luis.bc_tecnoin.product_api.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products/")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for product catalog (proxy to FakeStore API)")
public class ProductController {

    private final IProductService productService;

    @GetMapping
    @Operation(summary = "Get all products", description = "Returns all products from the FakeStore API")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    public Flux<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Returns a single product by its ID from the FakeStore API")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public Mono<ProductDto> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}
