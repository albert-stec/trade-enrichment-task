package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.service.CSVServiceImpl;
import com.verygoodbank.tes.service.TradeEnrichmentChunkService;
import com.verygoodbank.tes.service.specification.CSVService;
import com.verygoodbank.tes.util.ProductLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeEnrichmentController.class)
public class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CSVServiceImpl csvService;

    @MockBean
    private TradeEnrichmentChunkService tradeEnrichmentChunkService;

    @MockBean
    private ProductLoader productLoader;

    @Test
    public void testEnrichTradeData() throws Exception {
        String csvContent = CSVService.RESULT_HEADER + "\n20200101,1,USD,100\n20210102,2,USD,200\n202101022323,2,USD,200\n";

        MockMultipartFile file = new MockMultipartFile("file", "filename.csv", "text/plain",
                csvContent.getBytes());

        given(productLoader.getProductName("1")).willReturn("ProductA");
        given(productLoader.getProductName("2")).willReturn(ProductLoader.DEFAULT_PRODUCT_NAME);
        given(csvService.getValidRows(file)).willCallRealMethod();

        ReflectionTestUtils.setField(tradeEnrichmentChunkService, "productLoader", productLoader);
        given(tradeEnrichmentChunkService.process(any())).willCallRealMethod();

        mockMvc.perform(multipart("/api/v1/enrich/trade").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("[\"date,product_name,currency,price\",\"20200101,ProductA,USD,100\",\"20210102,Missing Product Name,USD,200\"]"));
    }


}