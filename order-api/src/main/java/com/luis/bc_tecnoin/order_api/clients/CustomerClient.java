package com.luis.bc_tecnoin.order_api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${services.customer.url}")
public interface CustomerClient {

        @GetMapping("/{id}/exists")
        boolean existsByUserId(@PathVariable("id") Long id);
}