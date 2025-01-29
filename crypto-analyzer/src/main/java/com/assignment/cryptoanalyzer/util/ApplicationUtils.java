package com.assignment.cryptoanalyzer.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApplicationUtils {
    private static String COMMA_DELIMITER = ",";

    public static List<List<String>> getFileContent(String fileName) {
       return readFromCSVFile(fileName);
    }

    private static List<List<String>> readFromCSVFile(String filePath) {
        File file = getFile(filePath);
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getPath()))) {
            List<List<String>> records = reader.lines()
                    .map(line -> line.split(COMMA_DELIMITER))
                    .filter(values -> values.length == 3)
                    .map(Arrays::asList)
                    .toList();

            return records;
        } catch (IOException e) {
            String msg = "There was an error on parsing CSV file: " + filePath + " ";
            msg += e.getMessage();
            throw new RuntimeException(msg);
        }
    }

    private static File getFile(String filePath) {
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:prices/" + filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
