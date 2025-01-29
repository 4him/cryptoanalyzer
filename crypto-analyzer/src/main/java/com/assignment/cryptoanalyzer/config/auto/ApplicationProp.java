package com.assignment.cryptoanalyzer.config.auto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "currencies")
public class ApplicationProp {

    private List<String> currencyCode;

}
