package com.assignment.cryptoanalyzer.service.impl;

import com.assignment.cryptoanalyzer.config.auto.ApplicationProp;
import com.assignment.cryptoanalyzer.entity.CryptoCurrency;
import com.assignment.cryptoanalyzer.util.ApplicationUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

/**
 * Service implementation for loading and processing cryptocurrency data from CSV files.
 *
 * <p>This class is responsible for reading cryptocurrency data from external CSV files,
 * validating the data, and converting it into a format suitable for further processing
 * and storage in the system.
 *
 * <p>Key Features:
 * <ul>
 *     <li>Reads CSV files corresponding to supported cryptocurrency codes defined in the configuration.</li>
 *     <li>Validates and filters raw data to ensure accuracy before processing.</li>
 *     <li>Transforms CSV rows into {@link CryptoCurrency} entities for system-wide usage.</li>
 * </ul>
 *
 * <p>Core Methods:
 * <ul>
 *     <li>{@link #readData()} - Entry point to load and process all cryptocurrency data from CSV files.</li>
 *     <li>{@link #loadDataFromCSV()} - Retrieves and processes data for all supported cryptocurrencies.</li>
 *     <li>{@link #getInfoFromFile(String)} - Reads and converts the content of a specific CSV file.</li>
 * </ul>
 *
 * <p>Implementation Details:
 * <ul>
 *     <li>Relies on {@link ApplicationProp} to fetch the list of supported currency codes.</li>
 *     <li>Uses {@link ApplicationUtils#getFileContent(String)} to read raw data from CSV files.</li>
 *     <li>Filters invalid rows (e.g., missing or non-numeric data) during processing.</li>
 * </ul>
 *
 * <p>Dependencies:
 * <ul>
 *     <li>{@link ApplicationProp} - Provides configuration for the application, including supported currencies.</li>
 *     <li>{@link ApplicationUtils} - Utility class for file operations.</li>
 * </ul>
 *
 * <p>Constants:
 * <ul>
 *     <li>{@link #FILE_NAME_EXTENSION} - Specifies the suffix for CSV file names (e.g., "_values.csv").</li>
 * </ul>
 *
 * <p>Usage:
 * This service is typically invoked during application startup or data synchronization
 * to load cryptocurrency data into the system from predefined CSV files.
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link Service} - Marks the class as a Spring-managed service.</li>
 *     <li>{@link Autowired} - Injects required dependencies automatically.</li>
 * </ul>
 *
 * <p>Example File Format:
 * <pre>
 * 1633072800000,BTC,42000.50
 * 1633076400000,ETH,3000.75
 * </pre>
 * The first column represents the timestamp, the second column the currency code,
 * and the third column the price.
 */

@Service
@AllArgsConstructor
public class FileServiceImpl {

    private static final String FILE_NAME_EXTENSION = "_values.csv";

    @Autowired
    private ApplicationProp applicationProp;

    public List<CryptoCurrency> readData() {
        return loadDataFromCSV();
    }

    public List<CryptoCurrency> loadDataFromCSV() {
        List<String> currencyData = applicationProp.getCurrencyCode();
        return currencyData.stream()
                .map(currency -> currency + FILE_NAME_EXTENSION)
                .flatMap(fileName -> getInfoFromFile(fileName).stream())
                .collect(Collectors.toList());
    }

    private List<CryptoCurrency> getInfoFromFile(String fileName) {
        return ApplicationUtils.getFileContent(fileName).parallelStream()
                .filter(row -> isCreatable(row.get(0)) && isCreatable(row.get(2))) // Проверка на корректность данных
                .map(row -> new CryptoCurrency(
                        null,
                        Long.parseLong(row.get(0)),  // timestamp
                        row.get(1),                 // currencyCode
                        BigDecimal.valueOf(Double.parseDouble(row.get(2))) // price
                ))
                .collect(Collectors.toList());
    }
}
