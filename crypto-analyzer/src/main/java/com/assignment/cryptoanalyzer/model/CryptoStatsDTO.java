package com.assignment.cryptoanalyzer.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CryptoStatsDTO {

    private String currencyCode;
    private LocalDateTime date;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal oldestPrice;
    private BigDecimal newestPrice;
    private BigDecimal normalizedRange;
    private String oldestDateTime;
    private String newestDateTime;

    public CryptoStatsDTO(String currencyCode, BigDecimal minPrice, BigDecimal maxPrice, Long oldestDateTime, Long newestDateTime) {
        this.currencyCode = currencyCode;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.oldestDateTime = formatTimestamp(oldestDateTime);
        this.newestDateTime = formatTimestamp(newestDateTime);
    }

    public String formatTimestamp(Long timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss SSSSS");
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
    }
}

    /*// Базовые поля
    private Long timeStamp;
    private String currencyCode; // Код криптовалюты (например, BTC, ETH)
    private LocalDate date; // Дата (группировка по дате)

    // Поля для расчётов
    private BigDecimal minPrice; // Минимальная цена за день/месяц
    private BigDecimal maxPrice; // Максимальная цена за день/месяц
    private BigDecimal oldestPrice; // Самая старая цена (первая запись)
    private BigDecimal newestPrice; // Самая новая цена (последняя запись)
    private BigDecimal normalizedRange; // Нормализованный диапазон ( (max-min)/min )

    // Поля для удобства фильтрации/агрегации
    private Long oldestTimestamp; // Временная метка самой старой записи
    private Long newestTimestamp; // Временная метка самой новой записи

    // Поля для отображения дополнительной информации пользователю
    private String oldestDateTime; // Читаемое значение даты и времени для старой записи
    private String newestDateTime; // Читаемое значение даты и времени для новой записи

    // Конструктор для создания DTO
    public CryptoStatsDTO(String currencyCode, LocalDate date, BigDecimal minPrice, BigDecimal maxPrice,
                          BigDecimal oldestPrice, BigDecimal newestPrice, Long oldestTimestamp,
                          Long newestTimestamp) {
        this.currencyCode = currencyCode;
        this.date = date;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.oldestPrice = oldestPrice;
        this.newestPrice = newestPrice;
        this.oldestTimestamp = oldestTimestamp;
        this.newestTimestamp = newestTimestamp;
        this.normalizedRange = calculateNormalizedRange(minPrice, maxPrice);

        // Форматирование дат для удобства
        this.oldestDateTime = formatTimestamp(oldestTimestamp);
        this.newestDateTime = formatTimestamp(newestTimestamp);
    }

    // Метод для расчёта нормализованного диапазона
    private BigDecimal calculateNormalizedRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null || min.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO; // Защита от деления на ноль
        }
        return max.subtract(min).divide(min, BigDecimal.ROUND_HALF_UP);
    }

    // Метод для форматирования временной метки в читаемую дату и время
    private String formatTimestamp(Long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss SSSS"));
    }*/
//}
