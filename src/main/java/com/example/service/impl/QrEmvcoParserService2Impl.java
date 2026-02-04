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

    private static final int MERCHANT_ACCOUNT_START = 26;
    private static final int MERCHANT_ACCOUNT_END = 51;


    @Override
    public ResponseEntity<?> parse(String emvco) {

        if (emvco == null || emvco.isBlank()) {
            throw new EmvParsingException(
                    EmvErrorCode.EMV_EMPTY_INPUT,
                    "El campo emvco no puede ser nulo o vacío"
            );
        }

        try {
            Map<String, String> tags = EmvTlvParser.parseTLV2(emvco);

            if (tags.isEmpty()) {
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

        } catch (EmvParsingException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error parseando EMVCo", e);
            throw new EmvParsingException(
                    EmvErrorCode.EMV_PARSE_ERROR,
                    e.getMessage()
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

    @Override
    public ResponseEntity<?> parse3(String qr) {
        if (qr == null || qr.length() < 4) {
            throw new EmvParsingException(
                    EmvErrorCode.EMV_EMPTY_INPUT,
                    "El campo emvco no puede ser nulo o vacío"
            );
        }

        try {

            EmvPayload response = EmvTlvParser.parse3(qr);

            if (!response.isCrcValid() &&
                    response.getCrcValueHex() == null &&
                    response.getMultillaveBreB() == null) {
                throw new EmvParsingException(
                        EmvErrorCode.EMV_INVALID_FORMAT,
                        "Formato invalido"
                );
            }

            QrEmvcoResponseDTO dto = QrEmvcoResponseDTO.builder()
                    .payloadFormatIndicator(response.getFlat().get("00"))
                    .pointOfInitiationMethod(response.getFlat().get("01"))
                    .merchantCategoryCode(response.getFlat().get("52"))
                    .transactionCurrency(response.getFlat().get("53"))
                    .transactionAmount(response.getFlat().get("54"))
                    .countryCode(response.getFlat().get("58"))
                    .merchantName(response.getFlat().get("59"))
                    .merchantCity(response.getFlat().get("60"))
                    .merchantAccount(
                            MerchantAccount2DTO.builder()
                                    .aliasValue(response.getMultillaveBreB())
                                    .globallyUniqueIdentifier(response.getFlat().get("49.00"))
                                    .build()
                    )
                    .build();

            return ResponseEntity.ok(dto);
        } catch (EmvParsingException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error parseando EMVCo", e);
            throw new EmvParsingException(
                    EmvErrorCode.EMV_PARSE_ERROR,
                    e.getMessage()
            );
        }
    }

    private MerchantAccount2DTO parseMerchantAccount(String rawValue) {

        Map<String, String> subTags = EmvTlvParser.parseTLV(rawValue);
        String gui = subTags.get("00");

        MerchantAccount2DTO.MerchantAccount2DTOBuilder builder =
                MerchantAccount2DTO.builder()
                        .globallyUniqueIdentifier(gui);

        if (gui == null) {
            return builder.build();
        }

        // Lógica por PSP / dominio
        if (gui.endsWith(".LLA")) {
            builder.aliasValue(subTags.get("05"));
        } else if (gui.endsWith(".RED")) {
            builder.aliasValue(subTags.get("01"));
        }

        return builder.build();
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
                .filter(t -> !tags.containsKey(t))
                .toList();

        if (!missing.isEmpty()) {
            throw new EmvParsingException(
                    EmvErrorCode.EMV_MANDATORY_TAG_MISSING,
                    "Tags faltantes: " + missing
            );
        }
    }


}
