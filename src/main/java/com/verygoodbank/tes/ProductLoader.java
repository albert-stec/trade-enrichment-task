package com.verygoodbank.tes;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductLoader {
    private static final ConcurrentHashMap<String, String> productMap = new ConcurrentHashMap<>();
    private static final String PRODUCT_FILE = "product.csv";

    @PostConstruct
    public void loadProductData() {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource(PRODUCT_FILE).toURI());
            List<String> lines = Files.readAllLines(path);
            lines.forEach(line -> {
                String[] parts = line.split(",");
                productMap.put(parts[0], parts[1]); // Assuming the format is productId,productName
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load product data", e);
        }
    }

    public static String getProductName(String productId) {
        return productMap.getOrDefault(productId, "Unknown Product");
    }
}
