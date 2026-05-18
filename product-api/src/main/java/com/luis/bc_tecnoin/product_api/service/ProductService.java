package com.luis.bc_tecnoin.product_api.service;

import com.luis.bc_tecnoin.product_api.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ProductService implements IProductService {

    private final WebClient webClient;

    public ProductService(WebClient.Builder builder, @Value("${external.api}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public Flux<ProductDto> getAllProducts() {
        return webClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(ProductDto.class);
    }

    @Override
    public Mono<ProductDto> getProductById(Long id) {
        return webClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }
}