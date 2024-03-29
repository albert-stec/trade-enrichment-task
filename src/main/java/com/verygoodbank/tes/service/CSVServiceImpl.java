package com.verygoodbank.tes.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.verygoodbank.tes.service.specification.CSVService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CSVServiceImpl implements CSVService {
    private static final Logger LOG = LoggerFactory.getLogger(CSVServiceImpl.class);
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}\\d{2}\\d{2}");

    @Override
    public List<String> getValidRows(MultipartFile file) {
        List<String[]> validRows = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            processRows(csvReader, validRows);
        } catch (IOException e) {
            LOG.error("Error reading CSV file", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing file");
        }
        return formatRows(validRows);
    }

    private void processRows(CSVReader csvReader, List<String[]> validRows) {
        csvReader.forEach(line -> {
            if (isValidRow(line)) {
                validRows.add(line);
            }
        });
    }

    private boolean isValidRow(String[] row) {
        String date = row[0];
        return StringUtils.isNotEmpty(date) && isDateValid(date);
    }

    private boolean isDateValid(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

    private List<String> formatRows(List<String[]> rows) {
        return rows.stream()
                .map(row -> String.join(",", row))
                .collect(Collectors.toList());
    }
}