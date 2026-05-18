package com.luis.bc_tecnoin.payment_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = "com.luis.bc_tecnoin.payment_api.clients")
public class PaymentApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentApiApplication.class, args);
	}

}
