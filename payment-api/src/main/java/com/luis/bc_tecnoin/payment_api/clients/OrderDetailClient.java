package com.luis.bc_tecnoin.payment_api.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "orders-details-service", url = "http://localhost:8085/api/v1/orders-detail")
public interface OrderDetailClient {

    @GetMapping("/{id}/exists")
    boolean existsOrderById(@PathVariable("id") Long id);

    @GetMapping("/order/{id}/total")
    Double calculateTotal(@PathVariable("id") Long id);
}