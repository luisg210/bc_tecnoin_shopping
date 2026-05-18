package com.luis.bc_tecnoin.payment_api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "http://localhost:8084/api/v1/orders")
public interface OrderClient {

        @GetMapping("/{id}/exists")
        boolean existsOrderById(@PathVariable("id") Long id);

        @GetMapping("/{id}/status")
        String getOrderStatus(@PathVariable("id") Long id);
}