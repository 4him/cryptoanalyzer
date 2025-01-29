package com.assignment.cryptoanalyzer.converter;

import com.assignment.cryptoanalyzer.dto.CryptoDTO;
import com.assignment.cryptoanalyzer.dto.StatsDTO;
import com.assignment.cryptoanalyzer.model.CryptoModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CryptoConverter {

    public CryptoDTO convertFromModelToDTO(CryptoModel cryptoModel) {
        return convertToStatsDTO(cryptoModel);
    }

    public List<CryptoDTO> convertFromModelToStatsList(List<CryptoModel> cryptoModelList) {
        return convertToStatsDTOList(cryptoModelList);
    }

    private CryptoDTO convertToStatsDTO(CryptoModel cryptoModel) {
//        if (cryptoModel == null) {
//            throw new IllegalArgumentException("CryptoModel cannot be null");
//        }

        StatsDTO statsDTO = StatsDTO.builder()
                .minPrice(cryptoModel.getMinPrice())
                .maxPrice(cryptoModel.getMaxPrice())
                .normalizedRange(cryptoModel.getNormalizedRange())
                .oldestDateTime(cryptoModel.getOldestDateTime())
                .newestDateTime(cryptoModel.getNewestDateTime())
                .build();

        return CryptoDTO.builder()
                .currencyCode(cryptoModel.getCurrencyCode())
                .statsDTO(statsDTO)
                .build();
    }

    private List<CryptoDTO> convertToStatsDTOList(List<CryptoModel> cryptoModels) {
        if (cryptoModels == null) {
            throw new IllegalArgumentException("List of CryptoModel cannot be null");
        }

        return cryptoModels.stream()
                .map(this::convertFromModelToDTO)
                .collect(Collectors.toList());
    }
}
