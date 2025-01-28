package io.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
public class FeesManagementBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeesManagementBackendApplication.class, args);
	}

}
