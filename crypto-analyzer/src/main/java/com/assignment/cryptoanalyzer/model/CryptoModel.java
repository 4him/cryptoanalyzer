package com.assignment.cryptoanalyzer.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class CryptoModel {

    private String currencyCode;
    private LocalDateTime date;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal oldestPrice;
    private BigDecimal newestPrice;
    private BigDecimal normalizedRange;
    private String oldestDateTime;
    private String newestDateTime;

    public String getDateTime(Long timeStamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss SSSSS");
        return Instant.ofEpochMilli(timeStamp).atZone(ZoneId.systemDefault()).toLocalDateTime().format(formatter);
    }
}
