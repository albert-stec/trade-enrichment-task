package com.verygoodbank.tes.service;

import com.verygoodbank.tes.service.specification.CSVService;
import com.verygoodbank.tes.util.ProductLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
class TradeEnrichmentChunkServiceTest {
    @Mock
    private ProductLoader productLoader;

    private TradeEnrichmentChunkService tradeEnrichmentChunkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productLoader.loadProductData();
        tradeEnrichmentChunkService = new TradeEnrichmentChunkService(productLoader);
    }

    @Test
    void processSingleChunkSuccessfully() {
        // Given
        List<String> lines = List.of("20200101,1,USD,100", "20200202,2,USD,200");
        when(productLoader.getProductName("1")).thenReturn("ProductNameA");
        when(productLoader.getProductName("2")).thenReturn("ProductNameB");

        // When
        List<String> processed = tradeEnrichmentChunkService.process(lines);

        // Then
        Assertions.assertNotNull(processed);
        Assertions.assertEquals(3, processed.size()); // Includes header + 2 lines
        Assertions.assertTrue(processed.contains(CSVService.RESULT_HEADER));
        Assertions.assertTrue(processed.contains("20200101,ProductNameA,USD,100"));
        Assertions.assertTrue(processed.contains("20200202,ProductNameB,USD,200"));
    }

    @Test
    void processMultipleChunksSuccessfully() {
        // Given
        List<String> lines = generateLines(2500); // Assuming CHUNK_SIZE = 1000
        for (int i = 0; i < 2500; i++) {
            when(productLoader.getProductName("Product" + i)).thenReturn("ProductName" + i);
        }

        // When
        List<String> processed = tradeEnrichmentChunkService.process(lines);

        // Then
        Assertions.assertNotNull(processed);
        Assertions.assertEquals(2501, processed.size()); // Includes header + 2500 lines
        Assertions.assertTrue(processed.contains(CSVService.RESULT_HEADER));
        for (int i = 0; i < 2500; i++) {
            Assertions.assertTrue(processed.contains("20200101,ProductName" + i + ",USD,100"));
        }
    }

    private List<String> generateLines(int numberOfLines) {
        return IntStream.range(0, numberOfLines)
                .mapToObj(i -> "20200101,Product" + i + ",USD,100")
                .collect(Collectors.toList());
    }
}