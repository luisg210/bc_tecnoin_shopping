package com.luis.bc_tecnoin.product_api.service;

import com.luis.bc_tecnoin.product_api.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Service layer responsible for communicating with FakeStore API.
 * Uses WebClient for non-blocking HTTP calls.
 */
@Service
@Slf4j
public class ProductService implements IProductService {

    private final WebClient webClient;

    @Value("${external.api}")
    private String productApi;

    public ProductService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://fakestoreapi.com").build();
    }

    /**
     * Retrieve all products from FakeStore API.
     */
    @Override
    public Flux<ProductDto> getAllProducts() {
        return webClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    /**
     * Retrieve a single product by ID.
     */
    @Override
    public Mono<ProductDto> getProductById(Long id) {
        return webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }
}