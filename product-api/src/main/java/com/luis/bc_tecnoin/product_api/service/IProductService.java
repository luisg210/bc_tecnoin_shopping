package com.luis.bc_tecnoin.product_api.service;

import com.luis.bc_tecnoin.product_api.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
    Flux<ProductDto> getAllProducts();
    Mono<ProductDto> getProductById(Long id);
}