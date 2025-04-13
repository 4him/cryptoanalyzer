package com.assignment.cryptoanalyzer.service.impl;

import com.assignment.cryptoanalyzer.config.auto.ApplicationProp;
import com.assignment.cryptoanalyzer.converter.CryptoConverter;
import com.assignment.cryptoanalyzer.dto.CryptoDTO;
import com.assignment.cryptoanalyzer.entity.CryptoCurrency;
import com.assignment.cryptoanalyzer.model.CryptoModel;
import com.assignment.cryptoanalyzer.service.interfaces.CryptoStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service implementation for analyzing cryptocurrency statistics, including
 * data normalization, filtering by date, and handling unsupported currency codes.
 *
 * <p>Key Features:
 * <ul>
 *     <li>Calculates and provides statistics for individual or multiple cryptocurrencies.</li>
 *     <li>Supports data normalization to rank currencies based on their price range.</li>
 *     <li>Filters cryptocurrency data by specific date ranges for advanced analysis.</li>
 *     <li>Validates currency codes and handles unsupported currencies with meaningful exceptions.</li>
 * </ul>
 *
 * <p>Core Methods:
 * <ul>
 *     <li>{@link #getStatsForCurrency(String)} - Retrieves statistical data for a specific currency.</li>
 *     <li>{@link #getCurrencyStatsOrderNormalizedRange()} - Returns a sorted list of currencies based on normalized price range.</li>
 *     <li>{@link #getStatsByDate(Integer, Integer, Integer)} - Retrieves statistics for currencies on a specific date.</li>
 * </ul>
 *
 * <p>Implementation Details:
 * <ul>
 *     <li>Uses {@link ApplicationProp} to fetch supported currency codes.</li>
 *     <li>Integrates with {@link CryptoServiceImpl} to fetch raw cryptocurrency data.</li>
 *     <li>Relies on {@link CryptoConverter} for converting data models to DTOs.</li>
 *     <li>Supports thread-safe operations and ensures proper exception handling for invalid input.</li>
 * </ul>
 *
 * <p>Workflow:
 * <ol>
 *     <li>Validates the currency code using {@link #validateCurrencyCode(String)} before proceeding with calculations.</li>
 *     <li>Calculates statistics such as maximum, minimum, and normalized price ranges for cryptocurrencies.</li>
 *     <li>Filters and processes data based on specific date ranges for detailed analysis.</li>
 * </ol>
 *
 * <p>Dependencies:
 * <ul>
 *     <li>{@link ApplicationProp} - Provides configuration for supported currencies.</li>
 *     <li>{@link CryptoServiceImpl} - Fetches raw cryptocurrency data for analysis.</li>
 *     <li>{@link CryptoConverter} - Converts internal models into DTOs for external use.</li>
 * </ul>
 *
 * <p>Annotations:
 * <ul>
 *     <li>{@link Service} - Marks the class as a Spring-managed service.</li>
 *     <li>{@link RequiredArgsConstructor} - Generates a constructor for final dependencies.</li>
 * </ul>
 *
 * <p>Usage:
 * This service is ideal for applications requiring advanced cryptocurrency statistics,
 * including normalized rankings and date-specific data filtering.
 */

@Service
@RequiredArgsConstructor
public class CryptoStatsServiceImpl implements CryptoStatsService {

    private final ApplicationProp applicationProp;
    private final CryptoServiceImpl cryptoService;
    private final CryptoConverter converter;

    @Override
    public CryptoDTO getStatsForCurrency(String currencyCode) {
        validateCurrencyCode(currencyCode);
        return buildCryptoStats(currencyCode);
    }

    @Override
    public List<CryptoDTO> getCurrencyStatsOrderNormalizedRange() {
        List<String> allCurrencyCodes = applicationProp.getCurrencyCode();
        List<CryptoModel> stats = allCurrencyCodes.stream()
                .map(this::buildNormalizedStats)
                .sorted(Comparator.comparing(CryptoModel::getNormalizedRange, Comparator.nullsLast(BigDecimal::compareTo)).reversed())
                .toList();

        return converter.convertFromModelToStatsList(stats);
    }

    @Override
    public CryptoDTO getStatsByDate(Integer day, Integer month, Integer year) {
        LocalDateTime start = LocalDateTime.of(year, month, day, 0, 0);
        LocalDateTime end = start.with(LocalTime.MAX);

        CryptoModel model = calculateStatsByDateRange(start, end);
        return converter.convertFromModelToDTO(model);
    }

    @Override
    public void validateCurrencyCode(String currencyCode) {
        List<String> supportedCurrencies = applicationProp.getCurrencyCode();
        if (supportedCurrencies.stream().noneMatch(code -> code.equalsIgnoreCase(currencyCode))) {
            throw new IllegalArgumentException("Unsupported currency code: " + currencyCode);
        }
    }

    private CryptoDTO buildCryptoStats(String currencyCode) {
        CryptoModel statsModel = calculateStats(currencyCode, false, true);
        return converter.convertFromModelToDTO(statsModel);
    }

    private CryptoModel buildNormalizedStats(String currencyCode) {
        return calculateStats(currencyCode, true, false);
    }

    private CryptoModel calculateStats(String currencyCode, boolean includeNormalized, boolean includeStats) {
        List<CryptoCurrency> data = cryptoService.getCryptoData(currencyCode);

        CryptoModel.CryptoModelBuilder builder = CryptoModel.builder();
        BigDecimal maxPrice = calculateMaxPrice(data);
        BigDecimal minPrice = calculateMinPrice(data);

        if (includeStats) {
            builder
                    .maxPrice(maxPrice)
                    .minPrice(minPrice)
                    .oldestPrice(calculateOldestPrice(data))
                    .newestPrice(calculateNewestPrice(data));
        } else if(includeNormalized) {
            builder
                    .normalizedRange(calculateNormalizedRange(minPrice, maxPrice));
        }

        return builder.build();
    }

    private CryptoModel calculateStatsByDateRange(LocalDateTime start, LocalDateTime end) {
        List<String> allCurrencyCodes = applicationProp.getCurrencyCode();

        return allCurrencyCodes.stream()
                .map(code -> filterByDateRange(start, end, code))
                .filter(list -> !list.isEmpty())
                .map(this::calculateStatsForDateRange)
                .max(Comparator.comparing(CryptoModel::getNormalizedRange, Comparator.nullsLast(BigDecimal::compareTo)))
                .orElse(null);
    }

    private List<CryptoCurrency> filterByDateRange(LocalDateTime from, LocalDateTime to, String currencyCode) {
        List<CryptoCurrency> data = cryptoService.getCryptoData(currencyCode);
        long fromMillis = Timestamp.valueOf(from).getTime();
        long toMillis = Timestamp.valueOf(to).getTime();

        return data.stream()
                .filter(item -> item.getTimestamp() >= fromMillis && item.getTimestamp() <= toMillis)
                .toList();
    }

    private CryptoModel calculateStatsForDateRange(List<CryptoCurrency> data) {
        BigDecimal maxPrice = calculateMaxPrice(data);
        BigDecimal minPrice = calculateMinPrice(data);
        BigDecimal normalizedRange = calculateNormalizedRange(minPrice, maxPrice);

        return CryptoModel.builder()
                .currencyCode(data.get(0).getCurrencyCode())
                .normalizedRange(normalizedRange)
                .build();
    }

    private BigDecimal calculateMaxPrice(List<CryptoCurrency> data) {
        return data.stream()
                .map(CryptoCurrency::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateMinPrice(List<CryptoCurrency> data) {
        return data.stream()
                .map(CryptoCurrency::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateOldestPrice(List<CryptoCurrency> data) {
        return data.stream()
                .min(Comparator.comparingLong(CryptoCurrency::getTimestamp))
                .map(CryptoCurrency::getPrice)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateNewestPrice(List<CryptoCurrency> data) {
        return data.stream()
                .max(Comparator.comparingLong(CryptoCurrency::getTimestamp))
                .map(CryptoCurrency::getPrice)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateNormalizedRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null || min.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return max.subtract(min).divide(min, 5, BigDecimal.ROUND_HALF_UP);
    }

    public String formatTimestamp(Long timestamp) {
        if (timestamp == null || timestamp <= 0) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).format(formatter);
    }
}
