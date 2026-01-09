package com.example.service;

import com.example.dto.QrRequestDTO;

public interface QrEmvcoParserService {
    QrRequestDTO parse(String emvco);
}
