package com.assignment.cryptoanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatsDTO {

    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @JsonProperty("max_price")
    private BigDecimal maxPrice;

    @JsonProperty("normalized_range")
    private BigDecimal normalizedRange;

    @JsonProperty("oldest_date_time")
    private String oldestDateTime;

    @JsonProperty("newest_date_time")
    private String newestDateTime;
}

