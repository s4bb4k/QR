package com.example.service.impl;

import com.example.dto.*;
import com.example.exception.ApiException;
import com.example.service.QrEmvcoParserService2;
import com.example.util.EmvTlvParser;
import com.example.util.ErrorCatalog;
import com.example.webClient.ThirdPartyQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class QrEmvcoParserService2Impl implements QrEmvcoParserService2 {

    private final ThirdPartyQrClient client;

    @Override
    public QrEmvcoResponseDTO parse(String emvco) {
        /*Map<String, String> tags = EmvTlvParser.parse(emvco);
        log.info("EMV TAGS PARSED: {}", tags);

        QrEmvcoResponseDTO response = new QrEmvcoResponseDTO();
        response.setPayloadFormatIndicator(tags.get("00"));
        response.setPointOfInitiationMethod(tags.get("01"));
        response.setMerchantCategoryCode(tags.get("52"));
        response.setTransactionCurrency(tags.get("53"));
        response.setTransactionAmount(tags.get("54"));
        response.setCountryCode(tags.get("58"));
        response.setMerchantName(tags.get("59"));
        response.setMerchantCity(tags.get("60"));

        if (tags.containsKey("26")) {
            response.setMerchantAccount(parseMerchantAccount(tags.get("26")));
        }

        return response;*/

        Map<String, String> tags = EmvTlvParser.parse(emvco);
        log.info("EMV TAGS PARSED: {}", tags);

        return QrEmvcoResponseDTO.builder()
                .payloadFormatIndicator(tags.get("00"))
                .pointOfInitiationMethod(tags.get("01"))
                .merchantCategoryCode(tags.get("52"))
                .transactionCurrency(tags.get("53"))
                .transactionAmount(tags.get("54"))
                .countryCode(tags.get("58"))
                .merchantName(tags.get("59"))
                .merchantCity(tags.get("60"))
                .merchantAccount(
                        tags.containsKey("26")
                                ? parseMerchantAccount(tags.get("26"))
                                : null
                )
                .build();

    }

    @Override
    public QrResponseParseDTO parse2(QrRequestParseDto request) {


        if (request.data() == null || request.data().useCaseInformation() == null) {
            throw new ApiException(ErrorCatalog.INVALID_CONTENT);
        }

        return client.parseQr(request.data().useCaseInformation().qrCode())
                .map(qrData -> {

                    QrResponseParseDTO.Meta meta = new QrResponseParseDTO.Meta(
                            request.meta().requestId(),
                            OffsetDateTime.now().toString(),
                            "SUCCESS",
                            "200",
                            "OK"
                    );

                    return new QrResponseParseDTO(meta, qrData);
                })
                .block();
    }

    private MerchantAccount2DTO parseMerchantAccount(String value) {

        Map<String, String> subTags = EmvTlvParser.parse(value);

        return MerchantAccount2DTO.builder()
                .globallyUniqueIdentifier(subTags.get("00"))
                .aliasType(mapAliasType(subTags.get("01"))) // 👈 AQUÍ
                .aliasValue(subTags.get("02"))               // 👈 AQUÍ
                .build();
    }

    private String mapAliasType(String code) {
        return switch (code) {
            case "01" -> "CELULAR";
            case "02" -> "CEDULA";
            case "03" -> "CUENTA";
            case "04" -> "EMAIL";
            default -> "DESCONOCIDO";
        };
    }
}
