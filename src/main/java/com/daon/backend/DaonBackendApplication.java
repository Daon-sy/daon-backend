package com.daon.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class DaonBackendApplication {

	public static void main(String[] args) {
		System.out.println("test");
		SpringApplication.run(DaonBackendApplication.class, args);
	}

}
