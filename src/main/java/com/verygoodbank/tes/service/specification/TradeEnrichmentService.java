package com.verygoodbank.tes.service.specification;

import java.util.List;

public interface TradeEnrichmentService {
    List<String> process(List<String> lines);
}
