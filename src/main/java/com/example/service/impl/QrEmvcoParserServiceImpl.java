package com.example.service.impl;

import com.example.dto.DataDTO;
import com.example.dto.MerchantAccountDTO;
import com.example.dto.MetaDTO;
import com.example.dto.QrRequestDTO;
import com.example.service.QrEmvcoParserService;
import com.example.util.EmvcoParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class QrEmvcoParserServiceImpl implements QrEmvcoParserService {

    @Override
    public QrRequestDTO parse(String emvco) {

        // ✅ CORRECCIÓN: validación defensiva
        if (emvco == null || emvco.isBlank()) {
            throw new IllegalArgumentException("EMVCo vacío");
        }

        Map<String, String> tags = EmvcoParser.parse(emvco);
        log.info("EMV TAGS PARSED: {}", tags);

        DataDTO data = new DataDTO();
        MerchantAccountDTO merchant = new MerchantAccountDTO();

        data.setMovementType("QR");
        data.setCurrency("COP");

        data.setAmount(new BigDecimal(tags.getOrDefault("54", "0")));

        data.setPointOfInitiationMethod(
                "11".equals(tags.get("01")) ? "ESTATICO" : "DINAMICO"
        );

        merchant.setMerchantCategoryCode(tags.get("52"));
        merchant.setMerchantName(tags.get("59"));
        merchant.setMerchantCity(tags.get("60"));
        merchant.setAliasValue(tags.get("26.01"));

        data.setMerchantAccountInformation(merchant);

        return QrRequestDTO.builder()
                .meta(MetaDTO.builder()
                        .requestId(UUID.randomUUID().toString())
                        .version("1.0.0")
                        .timestamp(OffsetDateTime.now().toString())
                        .build())
                .data(data)
                .build();

    }

}
