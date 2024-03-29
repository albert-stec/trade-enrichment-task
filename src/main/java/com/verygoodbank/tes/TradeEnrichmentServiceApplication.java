package com.verygoodbank.tes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeEnrichmentServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(TradeEnrichmentServiceApplication.class, args);
	}
}

// mvn clean package
// $> docker build --tag=enricher:latest .
// docker run -p8080:8080 enricher:latest