package com.example.service.impl;

import com.example.dto.*;
import com.example.exception.ApiException;
import com.example.exception.EmvParsingException;
import com.example.service.QrEmvcoParserService2;
import com.example.util.EmvTlvParser;
import com.example.util.ErrorCatalog;
import com.example.webClient.ThirdPartyQrClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class QrEmvcoParserService2Impl implements QrEmvcoParserService2 {

    private final ThirdPartyQrClient client;

    @Override
    public ResponseEntity<?> parse(String emvco) {
        /*log.info("emvco: {}", emvco);

        Map<String, String> tags = EmvTlvParser.parseTLV(emvco);
        log.info("EMV TAGS PARSED: {}", tags);

        QrEmvcoResponseDTO response = QrEmvcoResponseDTO.builder()
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

        return ResponseEntity.ok(response);*/

        if (emvco == null || emvco.isBlank()) {
            throw new EmvParsingException(
                    EmvErrorCode.EMV_EMPTY_INPUT,
                    "El campo emvco no puede ser nulo o vacío"
            );
        }

        try {
            log.info("EMVCo recibido");

            Map<String, String> tags = EmvTlvParser.parseTLV(emvco);

            if (tags == null || tags.isEmpty()) {
                throw new EmvParsingException(
                        EmvErrorCode.EMV_INVALID_FORMAT,
                        "No se encontraron tags EMVCo"
                );
            }

            validateMandatoryTags(tags);

            QrEmvcoResponseDTO response = QrEmvcoResponseDTO.builder()
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

            return ResponseEntity.ok(response);

        } catch (EmvParsingException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error inesperado parseando EMVCo", ex);
            throw new EmvParsingException(
                    EmvErrorCode.EMV_PARSE_ERROR,
                    ex.getMessage()
            );
        }


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

    @Override
    public QrGenerateResponseDTO generate(QrGenerateRequestDTO  request) {
        if (request == null || request.data() == null) {
            throw new ApiException(ErrorCatalog.INVALID_CONTENT);
        }

        return client.generateQr(request)
                .block();
    }

    @Override
    public QrValidateResponseDTO validate(QrValidateRequestDTO request) {
        if (request == null ||
                request.data() == null ||
                request.data().useCaseInformation() == null) {

            throw new ApiException(ErrorCatalog.INVALID_CONTENT);
        }

        return client.validate(request)
                .block();
    }

    @Override
    public QrUpdateStatusResponseDTO updateStatus(String id, QrUpdateStatusRequestDTO request) {
        if (id == null || id.isBlank() ||
                request == null ||
                request.data() == null) {

            throw new ApiException(ErrorCatalog.INVALID_CONTENT);
        }

        return client.updateStatus(id, request)
                .block();
    }

    @Override
    public QrQueryResponseDTO query(String id) {
        if (id == null || id.isBlank()) {
            throw new ApiException(ErrorCatalog.INVALID_CONTENT);
        }

        return client.queryById(id)
                .block();
    }

    private MerchantAccount2DTO parseMerchantAccount(String value) {

        Map<String, String> subTags = EmvTlvParser.parseTLV(value);

        return MerchantAccount2DTO.builder()
                .globallyUniqueIdentifier(subTags.get("00"))
                .aliasType(mapAliasType(subTags.get("01"))) // 👈 AQUÍ
                .aliasValue(subTags.get("05"))               // 👈 AQUÍ
                .build();
    }

    private String mapAliasType(String code) {
        if (code == null) {
            return "DESCONOCIDO";
        }
        code = code.trim();
        if (code.length() == 1) {
            code = "0" + code;
        }

        return switch (code) {
            case "01" -> "ALPHANUM";
            case "02" -> "NRIC";
            case "03" -> "PHONE";
            case "04" -> "EMAIL";
            case "05" -> "MERCHANT ";
            default -> "DESCONOCIDO";
        };
    }

    private static final List<String> MANDATORY_TAGS = List.of(
            "00", // Payload Format Indicator
            "52", // MCC
            "53", // Currency
            "58", // Country
            "59", // Merchant Name
            "60"  // Merchant City
    );

    private void validateMandatoryTags(Map<String, String> tags) {
        List<String> missing = MANDATORY_TAGS.stream()
                .filter(tag -> !tags.containsKey(tag))
                .toList();

        if (!missing.isEmpty()) {
            throw new EmvParsingException(
                    EmvErrorCode.EMV_MANDATORY_TAG_MISSING,
                    "Tags faltantes: " + missing
            );
        }
    }


}
