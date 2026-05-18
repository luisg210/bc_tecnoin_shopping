package com.luis.bc_tecnoin.detail_api.clients;

import com.luis.bc_tecnoin.detail_api.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${services.product.url}")
public interface ProductClient {

    @GetMapping("/{id}")
    ProductDTO getById(@PathVariable("id") Long id);
}
