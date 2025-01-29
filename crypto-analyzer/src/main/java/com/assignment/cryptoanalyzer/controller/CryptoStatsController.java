package com.assignment.cryptoanalyzer.controller;

import com.assignment.cryptoanalyzer.dto.CryptoDTO;
import com.assignment.cryptoanalyzer.service.impl.CryptoStatsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crypto")
@RequiredArgsConstructor
public class CryptoStatsController {

    @Autowired
    private final CryptoStatsServiceImpl cryptoStatsServiceImpl;

//     1. All currencies sorted descending by normalized range
    @GetMapping("/stats/normalized")
    public List<CryptoDTO> getAllByNormalizedRange() {
        return cryptoStatsServiceImpl.getCurrencyStatsOrderNormalizedRange();
    }

    // 2. Oldest/newest/min/max for currency
    @GetMapping("/stats/{symbol}")
    public ResponseEntity<Object> getStatsForSymbol(@PathVariable String symbol) {
        return ResponseEntity.ok(cryptoStatsServiceImpl.getStatsForCurrency(symbol));
    }

    // 3. Currency with highest normalized range during a special day
    @GetMapping("/stats/highest")
    public ResponseEntity<CryptoDTO> getHighestNormalizedRangeForDay(
            @RequestParam int day,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(cryptoStatsServiceImpl.getStatsByDate(day, month, year));
    }
}
