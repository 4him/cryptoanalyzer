package com.assignment.cryptoanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Crypto Price Statistics.
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsDTO {

    /**
     * The minimum price recorded in the dataset.
     */
    @Schema(description = "The minimum price recorded in the dataset", example = "0.1234")
    @JsonProperty("min_price")
    private BigDecimal minPrice;

    /**
     * The maximum price recorded in the dataset.
     */
    @Schema(description = "The maximum price recorded in the dataset", example = "1.2345")
    @JsonProperty("max_price")
    private BigDecimal maxPrice;

    /**
     * The normalized range, calculated as the difference between the maximum and minimum prices,
     * often normalized relative to some factor.
     */
    @Schema(description = "The normalized range (max - min) of the prices", example = "1.1111")
    @JsonProperty("normalized_range")
    private BigDecimal normalizedRange;

    /**
     * The timestamp representing the oldest record in the dataset.
     */
    @Schema(description = "The timestamp of the oldest record in the dataset", example = "2023-01-01T00:00:00Z")
    @JsonProperty("oldest_date_time")
    private String oldestDateTime;

    /**
     * The timestamp representing the newest record in the dataset.
     */
    @Schema(description = "The timestamp of the newest record in the dataset", example = "2023-01-31T23:59:59Z")
    @JsonProperty("newest_date_time")
    private String newestDateTime;
}

