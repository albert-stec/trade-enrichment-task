package com.verygoodbank.tes.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVService {
    String RESULT_HEADER = "date,product_name,currency,price";

    List<String> getValidRows(MultipartFile file);
}