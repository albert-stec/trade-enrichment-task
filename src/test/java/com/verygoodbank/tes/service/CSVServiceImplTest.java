package com.verygoodbank.tes.service;

import com.verygoodbank.tes.service.specification.CSVService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

class CSVServiceImplTest {

    private CSVServiceImpl csvService;

    @BeforeEach
    void setUp() {
        csvService = new CSVServiceImpl();
    }

    @Test
    void testGetValidRowsWithValidData() {
        String csvContent = CSVService.RESULT_HEADER + "\n20200101,ProductA,USD,100\n20210102,ProductB,USD,200\n";
        MultipartFile multipartFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        List<String> validRows = csvService.getValidRows(multipartFile);

        Assertions.assertNotNull(validRows);
        Assertions.assertEquals(2, validRows.size());
        Assertions.assertTrue(validRows.contains("20200101,ProductA,USD,100"));
        Assertions.assertTrue(validRows.contains("20210102,ProductB,USD,200"));
    }

    @Test
    void testGetValidRowsWithInvalidData() {
        String csvContent = CSVService.RESULT_HEADER + "\nwrongDate,ProductA,USD,100\n,ProductB,USD,200\n20160101,1,EUR,10.0";
        MultipartFile multipartFile = new MockMultipartFile("file", "test.csv", "text/csv", csvContent.getBytes());

        List<String> validRows = csvService.getValidRows(multipartFile);

        Assertions.assertNotNull(validRows);
        Assertions.assertEquals(1, validRows.size());
        Assertions.assertTrue(validRows.contains("20160101,1,EUR,10.0"));
    }

}