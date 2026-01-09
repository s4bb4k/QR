package com.example.service;

import com.example.dto.QrRequestDTO;
import com.example.dto.QrValidationResponseDTO;

public interface QrComparisonService {
    QrValidationResponseDTO compare(QrRequestDTO parsed, QrRequestDTO original);
}
