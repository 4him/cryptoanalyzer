package com.assignment.cryptoanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for representing cryptocurrency data along with its associated statistics.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptoDTO {

    /**
     * The currency code (e.g., BTC, ETH) representing the cryptocurrency.
     */
    @Schema(description = "The currency code (e.g., BTC, ETH) representing the cryptocurrency", example = "BTC")
    @JsonProperty("currency_code")
    private String currencyCode;

    /**
     * The statistical data associated with the cryptocurrency.
     */
    @Schema(description = "The statistical data associated with the cryptocurrency", implementation = StatsDTO.class)
    @JsonProperty("statistics")
    private StatsDTO statsDTO;
}
