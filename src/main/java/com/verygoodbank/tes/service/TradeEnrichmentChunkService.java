package com.verygoodbank.tes.service;

import com.verygoodbank.tes.util.ProductLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TradeEnrichmentChunkService implements TradeEnrichmentService {
    private static final int CHUNK_SIZE = 1000; // Define your chunk size based on your requirements and resources

    public List<String> process(List<String> lines) {
        List<List<String>> chunks = chunkify(lines, CHUNK_SIZE);
        List<String> processedLines = chunks.parallelStream()
                .flatMap(chunk -> processChunk(chunk).stream())
                .collect(Collectors.toList());

        processedLines.add(0, CSVService.RESULT_HEADER);

        return processedLines;
    }

    private List<String> processChunk(List<String> chunk) {
        return chunk.stream()
                .map(line -> {
                    String[] parts = line.split(",");
                    String productName = ProductLoader.getProductName(parts[1]);
                    return parts[0] + "," + productName + "," + String.join(",", parts[2], parts[3]);
                })
                .collect(Collectors.toList());
    }

    private List<List<String>> chunkify(List<String> lines, int chunkSize) {
        List<List<String>> chunks = new ArrayList<>();
        for (int i = 0; i < lines.size(); i += chunkSize) {
            chunks.add(new ArrayList<>(lines.subList(i, Math.min(i + chunkSize, lines.size()))));
        }
        return chunks;
    }
}