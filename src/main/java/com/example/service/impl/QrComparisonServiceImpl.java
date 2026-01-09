package com.example.service.impl;

import com.example.dto.QrRequestDTO;
import com.example.dto.QrValidationResponseDTO;
import com.example.service.QrComparisonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class QrComparisonServiceImpl  implements QrComparisonService {

    @Override
    public QrValidationResponseDTO compare(QrRequestDTO parsed, QrRequestDTO original) {
        List<String> diffs = new ArrayList<>();

        compare("amount",
                parsed.getData().getAmount(),
                original.getData().getAmount(),
                diffs);

        compare("merchantName",
                parsed.getData().getMerchantAccountInformation().getMerchantName(),
                original.getData().getMerchantAccountInformation().getMerchantName(),
                diffs);

        compare("merchantCity",
                parsed.getData().getMerchantAccountInformation().getMerchantCity(),
                original.getData().getMerchantAccountInformation().getMerchantCity(),
                diffs);

        return QrValidationResponseDTO.builder()
                .equals(diffs.isEmpty())
                .parsed(parsed)
                .differences(diffs)
                .build();
    }

    private void compare(String field, Object parsed, Object original, List<String> diffs) {
        if (!Objects.equals(parsed, original)) {
            diffs.add(field + " differs: parsed=" + parsed + ", original=" + original);
        }
    }
}
