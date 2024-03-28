package com.verygoodbank.tes.util;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ProductLoader.class);

    private static final ConcurrentHashMap<String, String> productMap = new ConcurrentHashMap<>();
    public static final String DEFAULT_PRODUCT_NAME = "Missing Product Name";

    @Value("${product.file.name}")
    private String PRODUCT_FILE;

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
        if (productMap.isEmpty()) {
            throw new IllegalStateException("Product data not loaded");
        }

        if (productMap.containsKey(productId)) {
            return productMap.get(productId);
        } else {
            LOG.error("Product name not found for product id: {}", productId);
            return DEFAULT_PRODUCT_NAME;
        }
    }
}
