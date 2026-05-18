package com.luis.bc_tecnoin.customer_api.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "${services.auth.url}")
public interface AuthClient {

    @GetMapping("/{id}/exists")
    boolean existsById(@PathVariable("id") Long id);
}