package com.library.loan_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "clients")
@EnableScheduling
@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "models")
@ComponentScan(basePackages = {
    "com.library.loan_service",
    "models",
    "controllers",
    "services",
    "repositories",
    "dtos",
    "events",
    "exceptions",
    "clients",
    "config"
})
public class LoanServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanServiceApplication.class, args);
	}

}
