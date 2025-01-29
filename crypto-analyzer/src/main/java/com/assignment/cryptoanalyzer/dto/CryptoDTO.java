package com.assignment.cryptoanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptoDTO {

    @JsonProperty("currency_code")
    private String currencyCode;

    @JsonProperty("statistics")
    private StatsDTO statsDTO;
}

