package com.sparta.backendonboardingassignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BackendOnboardingAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendOnboardingAssignmentApplication.class, args);
	}

}
