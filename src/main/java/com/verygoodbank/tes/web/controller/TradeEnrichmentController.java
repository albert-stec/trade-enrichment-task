package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.service.CSVService;
import com.verygoodbank.tes.service.TradeEnrichmentChunkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {

    private final CSVService csvService;
    private final TradeEnrichmentChunkService tradeEnrichmentChunkService;

    public TradeEnrichmentController(CSVService csvService, TradeEnrichmentChunkService tradeEnrichmentChunkService) {
        this.csvService = csvService;
        this.tradeEnrichmentChunkService = tradeEnrichmentChunkService;
    }

    @PostMapping("/enrich/trade")
    public List<String> enrichTradeData(@RequestParam("file") MultipartFile file) {
        List<String> lines = csvService.getValidRows(file);
        List<String> result = tradeEnrichmentChunkService.process(lines);

        return result;
    }

}


