package com.verygoodbank.tes.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CSVServiceImpl implements CSVService {
    private static final Logger LOG = LoggerFactory.getLogger(CSVServiceImpl.class);

    public List<String> getValidRows(MultipartFile file) {
        List<String[]> validRows = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            csvReader.forEach(line -> validateRow(line, validRows));
        } catch (IOException e) {
            String message = "Unexpected error occurred while processing file.";
            LOG.error(message, e);
            throw new HttpServerErrorException(HttpStatusCode.valueOf(500),
                    message);
        }

        return  validRows.stream()
                .map(row -> String.join(",", row))
                .collect(Collectors.toList());
    }

    private void validateRow(String[] line, List<String[]> validRows) {
        String date = line[0];
        if (StringUtils.isNotEmpty(date) && isDateValid(date)) {
            validRows.add(line);
        }
    }

    private boolean isDateValid(String date) {
        return Pattern.compile("[0-9]{4}[0-9]{2}[0-9]{2}")
                .matcher(date).matches();

    }

    private File createFile(List<String[]> data) {
        File file = null;
        FileWriter fileWriter;
        try {
            file = Files.createTempFile(String.valueOf(Instant.now().toEpochMilli()),
                    ".csv").toFile();
            fileWriter = new FileWriter(file);
            writeDataToFile(fileWriter, data);
            fileWriter.close();
        } catch (IOException e) {
//            LOG.error("Unexpected file error occurred.", e);
        }

        return file;
    }

    private void writeDataToFile(FileWriter fileWriter, List<String[]> data) throws IOException {
        CSVWriter writer = new CSVWriter(fileWriter);
        String[] header = {"date", "product_id", "currency", "price"};
        writer.writeNext(header);
        data.forEach(writer::writeNext);
    }
}
