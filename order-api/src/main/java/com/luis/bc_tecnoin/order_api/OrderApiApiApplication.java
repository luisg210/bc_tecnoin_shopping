package com.luis.bc_tecnoin.order_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = "com.luis.bc_tecnoin.order_api.clients")
public class OrderApiApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApiApiApplication.class, args);
	}

}
