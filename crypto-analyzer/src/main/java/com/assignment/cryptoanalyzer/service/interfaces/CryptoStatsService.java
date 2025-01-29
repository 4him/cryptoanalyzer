package com.assignment.cryptoanalyzer.service.interfaces;

import com.assignment.cryptoanalyzer.dto.CryptoDTO;

import java.util.List;

public interface CryptoStatsService {

    CryptoDTO getStatsForCurrency(String currencyCode);

    List<CryptoDTO> getCurrencyStatsOrderNormalizedRange();

    CryptoDTO getStatsByDate(Integer day, Integer month, Integer year);

    String formatTimestamp(Long timestamp);

    void validateCurrencyCode(String currencyCode);
}
