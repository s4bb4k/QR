package com.example.service;

import com.example.dto.QrEmvcoResponseDTO;
import com.example.dto.QrRequestParseDto;
import com.example.dto.QrResponseParseDTO;

public interface QrEmvcoParserService2 {
    QrEmvcoResponseDTO parse(String emvco);
    QrResponseParseDTO parse2(QrRequestParseDto qrRequestParseDto);
}
