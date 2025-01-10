package com.ssmoker.smoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmokerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmokerApplication.class, args);
	}

}
