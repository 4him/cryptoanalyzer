package com.assignment.cryptoanalyzer.service.impl;

import com.assignment.cryptoanalyzer.repository.CryptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CryptoService {

    /**
     * This file is a sample o how
     */

    @Autowired
    private final CryptoRepository cryptoRepository;

    // 1. Вычисление oldest/newest/min/max для каждого крипто за месяц
//    public List<CryptoModel> calculateStatsForMonth(int month, int year) {
//        List<CryptoModel> stats = cryptoRepository.calculateStatsForMonth(month, year);
//
//        return stats.stream()
//                .map(row ->
//                        CryptoModel.builder()
//                                .currencyCode(row.getCurrencyCode())
//                                .minPrice(row.getMinPrice())
//                                .maxPrice(row.getMaxPrice())
//                                .oldestDateTime(row.getOldestDateTime())
//                                .newestDateTime(row.getNewestDateTime())
//                                .build()
//                ).toList();
//    }

    // 2. Crypto range sorted by normalized range in descending order
   /* public List<Map<String, CryptoModel>> getAllByNormalizedRange() {
        List<CryptoModel> stats = cryptoRepository.findAllByNormalizedRange();

        return stats.stream()
                .map(row -> Map.of(row.getCurrencyCode(),
                        CryptoModel.builder()
                                .currencyCode(row.getCurrencyCode())
                                .normalizedRange(row.getNormalizedRange())
                                .build()
                )).toList();
    }*/

/*
    // 3. Oldest/newest/min/max for currency
    public CryptoModel getStatsBySymbol(String symbol) {
        List<CryptoModel> stats = cryptoRepository.findStatsBycurrencyCode(symbol);

        if (stats.isEmpty()) {
            throw new IllegalArgumentException("Криптовалюта с символом " + symbol + " не найдена");
        }

        CryptoModel row = stats.get(0);
        return CryptoModel.builder()
                .currencyCode(row.getCurrencyCode())
                .minPrice(row.getMinPrice())
                .maxPrice(row.getMaxPrice())
                .oldestDateTime(row.getOldestDateTime())
                .newestDateTime(row.getNewestDateTime())
                .build();
    }
*/

    // 4. Highest normalized range for crypto
   /* public Map<String, Object> getHighestNormalizedRangeForDay(int day, int month, int year) {
        List<Object[]> stats = cryptoRepository.findHighestNormalizedRangeForDay(day, month, year);

        if (stats.isEmpty()) {
            throw new IllegalArgumentException("Данные за указанный день отсутствуют");
        }

        Object[] row = stats.get(0);
        return Map.of(
                "symbol", row[0],
                "normalizedRange", row[1]
        );
    }*/
}
