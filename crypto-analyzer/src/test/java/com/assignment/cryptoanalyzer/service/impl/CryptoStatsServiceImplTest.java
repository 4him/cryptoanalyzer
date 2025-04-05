package com.assignment.cryptoanalyzer.service.impl;

import com.assignment.cryptoanalyzer.config.auto.ApplicationProp;
import com.assignment.cryptoanalyzer.converter.CryptoConverter;
import com.assignment.cryptoanalyzer.dto.CryptoDTO;
import com.assignment.cryptoanalyzer.dto.StatsDTO;
import com.assignment.cryptoanalyzer.entity.CryptoCurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CryptoStatsServiceImplTest {

    @Mock
    private ApplicationProp applicationProp;
    @Mock
    private CryptoServiceImpl cryptoService;
    @Mock
    private CryptoConverter converter;

    private CryptoStatsServiceImpl cryptoStatsService;

    @BeforeEach
    public void startUp() {
        cryptoStatsService = new CryptoStatsServiceImpl(applicationProp, cryptoService, converter);
    }

    @Test
    void validateCurrencyCode_throwsExceptionForInvalidCode() {
        // Arrange
        when(applicationProp.getCurrencyCode()).thenReturn(List.of("BTC", "ETH"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> cryptoStatsService.validateCurrencyCode("XRP"));
    }

    @Test
    void getStatsForCurrency_returnsCorrectStats() {
        // Arrange
        List<CryptoCurrency> mockData = List.of(
                new CryptoCurrency(null, 1622505600000L, "BTC", BigDecimal.valueOf(50000)), // 01-06-2021
                new CryptoCurrency(null, 1622592000000L, "BTC", BigDecimal.valueOf(45000))  // 02-06-2021
        );
        StatsDTO statsDTO = StatsDTO.builder()
                .minPrice(BigDecimal.valueOf(45000))
                .maxPrice(BigDecimal.valueOf(50000))
                .oldestDateTime("01-06-2021 00:00:00")
                .build();

        CryptoDTO cryptoDTO = CryptoDTO.builder()
                .statsDTO(statsDTO)
                .currencyCode("BTC")
                .build();

        when(applicationProp.getCurrencyCode()).thenReturn(List.of("BTC"));
        when(cryptoService.getCryptoData("BTC")).thenReturn(mockData);
        when(converter.convertFromModelToDTO(any())).thenReturn(cryptoDTO);

        // Act
        CryptoDTO result = cryptoStatsService.getStatsForCurrency("BTC");

        // Assert
        assertEquals(new BigDecimal("50000"), result.getStatsDTO().getMaxPrice());
        assertEquals(new BigDecimal("45000"), result.getStatsDTO().getMinPrice());
        assertEquals("01-06-2021 00:00:00", result.getStatsDTO().getOldestDateTime());
    }

    @Test
    void getCurrencyStatsOrderNormalizedRange() {
    }

    @Test
    void getStatsByDate() {
    }

    @Test
    void validateCurrencyCode() {
    }

    @Test
    void formatTimestamp() {
    }
}
