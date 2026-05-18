package com.luis.bc_tecnoin.detail_api.clients;

import com.luis.bc_tecnoin.detail_api.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service", url = "${services.order.url}")
public interface OrderClient {

    @GetMapping("/{id}/exists")
    boolean existsOrderById(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    OrderDTO getOrderById(@PathVariable("id") Long id);
}
