package com.example.service;

import com.example.dto.*;
import org.springframework.http.ResponseEntity;

public interface QrEmvcoParserService2 {
    ResponseEntity<?> parse(String emvco);
    QrResponseParseDTO parse2(QrRequestParseDto qrRequestParseDto);
    QrGenerateResponseDTO generate(QrGenerateRequestDTO request);
    QrValidateResponseDTO validate(QrValidateRequestDTO qrValidateRequestDto);
    QrUpdateStatusResponseDTO updateStatus(String id, QrUpdateStatusRequestDTO qrUpdateStatusRequestDto);
    QrQueryResponseDTO query(String id);
    ResponseEntity<?> parse3(String qr);
}
