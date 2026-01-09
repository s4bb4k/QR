package com.example.service;

import com.example.dto.QrRequestDTO;
import com.example.dto.QrResponseDTO;

public interface QrEmvcoService {
    QrResponseDTO generateEmvco(QrRequestDTO request);
}
