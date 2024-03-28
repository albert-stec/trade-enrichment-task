package com.verygoodbank.tes.service;

import java.util.List;

public interface TradeEnrichmentService {
    List<String> process(List<String> lines);
}
