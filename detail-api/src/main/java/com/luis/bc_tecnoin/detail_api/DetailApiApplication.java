package com.luis.bc_tecnoin.detail_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DetailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DetailApiApplication.class, args);
	}

}
