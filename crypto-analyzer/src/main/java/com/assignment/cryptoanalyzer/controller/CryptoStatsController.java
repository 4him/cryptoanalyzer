package com.assignment.cryptoanalyzer.controller;

import com.assignment.cryptoanalyzer.dto.CryptoDTO;
import com.assignment.cryptoanalyzer.service.impl.CryptoStatsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller providing endpoints for retrieving cryptocurrency statistics.
 */
@RestController
@RequestMapping("/crypto")
@RequiredArgsConstructor
public class CryptoStatsController {

    @Autowired
    private final CryptoStatsServiceImpl cryptoStatsServiceImpl;

    /**
     * Retrieves all cryptocurrencies sorted in descending order by their normalized range.
     *
     * @return A list of CryptoDTO objects, each containing the currency code and its statistics.
     */
    @Operation(
            summary = "Get all currencies sorted by normalized range",
            description = "Retrieves a list of cryptocurrencies with their associated statistics, sorted in descending order by the normalized range (max - min)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of cryptocurrencies")
    })
    @GetMapping("/stats/normalized")
    public List<CryptoDTO> getAllByNormalizedRange() {
        return cryptoStatsServiceImpl.getCurrencyStatsOrderNormalizedRange();
    }

    /**
     * Retrieves detailed statistical data (oldest, newest, min, max prices) for a specific cryptocurrency symbol.
     *
     * @param symbol The symbol of the cryptocurrency (e.g., BTC, ETH).
     * @return A ResponseEntity containing the cryptocurrency statistics.
     */
    @Operation(
            summary = "Get statistics for a specific cryptocurrency",
            description = "Retrieves detailed statistical data such as the oldest and newest records, as well as the minimum and maximum prices for the specified cryptocurrency symbol."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cryptocurrency statistics"),
            @ApiResponse(responseCode = "404", description = "Cryptocurrency not found")
    })
    @GetMapping("/stats/{symbol}")
    public ResponseEntity<Object> getStatsForSymbol(
            @Parameter(description = "The symbol of the cryptocurrency", example = "BTC")
            @PathVariable String symbol) {
        return ResponseEntity.ok(cryptoStatsServiceImpl.getStatsForCurrency(symbol));
    }

    /**
     * Retrieves the cryptocurrency with the highest normalized range for a given date.
     *
     * @param day   The day of the target date.
     * @param month The month of the target date.
     * @param year  The year of the target date.
     * @return A ResponseEntity containing the cryptocurrency data with the highest normalized range for the specified date.
     */
    @Operation(
            summary = "Get cryptocurrency with highest normalized range on a specific day",
            description = "Retrieves the cryptocurrency that recorded the highest normalized range on the specified day, month, and year."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cryptocurrency data"),
            @ApiResponse(responseCode = "404", description = "Data not found for the given date")
    })
    @GetMapping("/stats/highest")
    public ResponseEntity<CryptoDTO> getHighestNormalizedRangeForDay(
            @Parameter(description = "Day of the date", example = "15") @RequestParam int day,
            @Parameter(description = "Month of the date", example = "5") @RequestParam int month,
            @Parameter(description = "Year of the date", example = "2023") @RequestParam int year) {
        return ResponseEntity.ok(cryptoStatsServiceImpl.getStatsByDate(day, month, year));
    }
}
