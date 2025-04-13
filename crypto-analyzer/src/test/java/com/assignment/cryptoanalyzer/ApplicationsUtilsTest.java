package com.assignment.cryptoanalyzer;

import com.assignment.cryptoanalyzer.config.auto.ApplicationProp;
import com.assignment.cryptoanalyzer.converter.CryptoConverter;
import com.assignment.cryptoanalyzer.dto.CryptoDTO;
import com.assignment.cryptoanalyzer.dto.StatsDTO;
import com.assignment.cryptoanalyzer.entity.CryptoCurrency;
import com.assignment.cryptoanalyzer.repository.CryptoRepository;
import com.assignment.cryptoanalyzer.service.impl.CryptoServiceImpl;
import com.assignment.cryptoanalyzer.service.impl.CryptoStatsServiceImpl;
import com.assignment.cryptoanalyzer.service.impl.FileServiceImpl;
import com.assignment.cryptoanalyzer.service.interfaces.CryptoStatsService;
import com.assignment.cryptoanalyzer.util.ApplicationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
//@EnableConfigurationProperties(value = ApplicationProp.class)
@SpringBootTest
@EnableConfigurationProperties(value = ApplicationProp.class)
public class ApplicationsUtilsTest {

    ApplicationUtils utils;
    CryptoServiceImpl cryptoService;
    CryptoRepository cryptoRepository;
    FileServiceImpl fileService;
    CryptoStatsService cryptoStatsService;

    @Mock
    CryptoConverter converter;

    @Mock
    private ApplicationProp applicationProp;

    private Map<String, List<CryptoCurrency>> currencyCache = new ConcurrentHashMap<>();

    @BeforeEach
    public void startUp() {
        utils = mock(ApplicationUtils.class);
        cryptoRepository = mock(CryptoRepository.class);
        fileService = new FileServiceImpl(applicationProp);
        cryptoService = new CryptoServiceImpl(cryptoRepository, fileService);
        cryptoStatsService = new CryptoStatsServiceImpl(applicationProp, cryptoService, converter);

        when(applicationProp.getCurrencyCode()).thenReturn(List.of("BTC", "ETH"));
        currencyCache.put("BTC", List.of(new CryptoCurrency(null, 1641009600000L, "BTC", BigDecimal.valueOf(46813.21))));
    }

    @Test
    void testGetAllByCurrency_readsRealCsvFile() {

        // Замена пути файла на путь к тестовым ресурсам
        String testFilePath = "BTC_values.csv";

        // Вызов метода
        List<List<String>> allListValues = ApplicationUtils.getFileContent(testFilePath);

        // Проверки
        assertNotNull(allListValues, "Data file should not be null");
        assertEquals(101, allListValues.size(), "The number of lines/elements is not corresponding");
        assertEquals("1641009600000", allListValues.get(1).get(0), "Date in the first line does not match");
        assertEquals("BTC", allListValues.get(1).get(1), "Cryptocurrency type in the first line does not match");
        assertEquals("46813.21", allListValues.get(1).get(2), "Price in the first line does not match");
    }

    @Test
    void testGetAllCurrencyMap() {
        // Подготовка данных
        String fileName = "BTC_values.csv";
        List<CryptoCurrency> allData = List.of(
                new CryptoCurrency(null, 1641009600000L, "BTC", BigDecimal.valueOf(46813.21))
        );

        when(fileService.loadDataFromCSV()).thenReturn(allData);
        when(cryptoRepository.findByCurrencyCode(anyString())).thenReturn(allData);
        when(cryptoService.getCryptoData("BTC")).thenReturn(allData);

        // Выполняем тест
        List<CryptoCurrency> result = cryptoService.getCryptoData("BTC");

        assertEquals(1, result.size(), "The number of elements is not corresponding");
        CryptoCurrency cryptoElement = result.get(0);

        // Проверяем данные
        assertEquals(1641009600000L, cryptoElement.getTimestamp(), "The timeStamp field value does not correspond");
        assertEquals("BTC", cryptoElement.getCurrencyCode(), "The currency Code field value does not correspond");
        assertEquals(BigDecimal.valueOf(46813.21), cryptoElement.getPrice(), "The price field value does not correspond");
    }

    @Test
    void testValidateCurrencyCode_throwsExceptionForUnsupportedCurrency() {
        when(applicationProp.getCurrencyCode()).thenReturn(List.of("BTC", "ETH"));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> cryptoStatsService.validateCurrencyCode("DOGE"),
                "Expected exception for unsupported currency"
        );

        assertEquals("Unsupported currency code: DOGE", exception.getMessage());
    }

    @Test
    void getStatsForCurrency_returnsCorrectStats() {
        // Mock данных
        CryptoCurrency cryptoCurrency1 = new CryptoCurrency(null, 1641009600000L, "BTC", BigDecimal.valueOf(46813.21));
        CryptoCurrency cryptoCurrency2 = new CryptoCurrency(null, 1641013200000L, "BTC", BigDecimal.valueOf(47000.00));
        StatsDTO statsDTO = StatsDTO.builder().minPrice(BigDecimal.valueOf(46813.21)).maxPrice(BigDecimal.valueOf(47000.00)).build();
        CryptoDTO cryptoDTO = CryptoDTO.builder().statsDTO(statsDTO).currencyCode("BTC").build();

        when(applicationProp.getCurrencyCode()).thenReturn(List.of("BTC"));
        when(cryptoRepository.findByCurrencyCode(anyString())).thenReturn(List.of(cryptoCurrency1, cryptoCurrency2));
        when(cryptoService.getCryptoData("BTC")).thenReturn(List.of(cryptoCurrency1, cryptoCurrency2));
        when(converter.convertFromModelToDTO(any())).thenReturn(cryptoDTO);

        CryptoDTO result = cryptoStatsService.getStatsForCurrency("BTC");

        assertNotNull(result, "Result should not be null");
        assertEquals("BTC", result.getCurrencyCode(), "Currency code does not match");
        assertEquals(BigDecimal.valueOf(47000.00), result.getStatsDTO().getMaxPrice(), "Max price does not match");
        assertEquals(BigDecimal.valueOf(46813.21), result.getStatsDTO().getMinPrice(), "Min price does not match");
    }

    @Test
    void testGetCurrencyStatsOrderNormalizedRange_returnsSortedStats() {
        CryptoCurrency crypto1 = new CryptoCurrency(null, 1641009600000L, "BTC", BigDecimal.valueOf(45000.00));
        CryptoCurrency crypto2 = new CryptoCurrency(null, 1641013200000L, "BTC", BigDecimal.valueOf(50000.00));
        CryptoCurrency crypto3 = new CryptoCurrency(null, 1641009600000L, "ETH", BigDecimal.valueOf(3000.00));
        CryptoCurrency crypto4 = new CryptoCurrency(null, 1641013200000L, "ETH", BigDecimal.valueOf(3500.00));

        StatsDTO statsDTO1 = StatsDTO.builder().minPrice(BigDecimal.valueOf(45000.00)).maxPrice(BigDecimal.valueOf(47000.00)).build();
        StatsDTO statsDTO2 = StatsDTO.builder().minPrice(BigDecimal.valueOf(3000.00)).maxPrice(BigDecimal.valueOf(3500.00)).build();
        CryptoDTO cryptoDTO1 = CryptoDTO.builder().statsDTO(statsDTO1).currencyCode("BTC").build();
        CryptoDTO cryptoDTO2 = CryptoDTO.builder().statsDTO(statsDTO2).currencyCode("ETH").build();

        when(applicationProp.getCurrencyCode()).thenReturn(List.of("BTC", "ETH"));
        when(cryptoService.getCryptoData("BTC")).thenReturn(List.of(crypto1, crypto2));
        when(cryptoService.getCryptoData("ETH")).thenReturn(List.of(crypto3, crypto4));
        when(converter.convertFromModelToStatsList(any())).thenReturn(List.of(cryptoDTO1,cryptoDTO2));

        List<CryptoDTO> stats = cryptoStatsService.getCurrencyStatsOrderNormalizedRange();

        assertNotNull(stats, "Stats list should not be null");
        assertEquals(2, stats.size(), "Number of stats does not match");
        assertEquals("BTC", stats.get(0).getCurrencyCode(), "BTC should have the highest normalized range");
        assertEquals("ETH", stats.get(1).getCurrencyCode(), "ETH should have the second highest normalized range");
        assertEquals(BigDecimal.valueOf(47000.00), stats.get(0).getStatsDTO().getMaxPrice(), "ETH should have the second highest normalized range");
        assertEquals(BigDecimal.valueOf(3000.00), stats.get(1).getStatsDTO().getMinPrice(), "ETH should have the second highest normalized range");
    }

}

