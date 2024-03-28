package com.verygoodbank.tes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeEnrichmentServiceApplication implements ApplicationRunner {

	@Autowired
	ProductLoader productLoader;

	public static void main(String[] args) {
		SpringApplication.run(TradeEnrichmentServiceApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.err.println(ProductLoader.getProductName("1"));;
	}
}
