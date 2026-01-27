package com.example.service;

import com.example.dto.*;

public interface QrEmvcoParserService2 {
    QrEmvcoResponseDTO parse(String emvco);
    QrResponseParseDTO parse2(QrRequestParseDto qrRequestParseDto);
    QrGenerateResponseDTO generate(QrGenerateRequestDTO request);
    QrValidateResponseDTO validate(QrValidateRequestDTO qrValidateRequestDto);
    QrUpdateStatusResponseDTO updateStatus(String id, QrUpdateStatusRequestDTO qrUpdateStatusRequestDto);
    QrQueryResponseDTO query(String id);
}
