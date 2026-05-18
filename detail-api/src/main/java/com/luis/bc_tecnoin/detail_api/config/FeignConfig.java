package com.luis.bc_tecnoin.detail_api.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.luis.bc_tecnoin.detail_api.clients")
public class FeignConfig {
}
