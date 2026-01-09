package com.example.service.impl;

import com.example.dto.QrRequestDTO;
import com.example.dto.QrResponseDTO;
import com.example.service.QrEmvcoService;
import com.example.util.EmvcoBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QrEmvcoServiceImpl implements QrEmvcoService {

    @Override
    public QrResponseDTO generateEmvco(QrRequestDTO request) {
        String emvco = EmvcoBuilder.build(request.getData());

        return QrResponseDTO.builder()
                .requestId(request.getMeta().getRequestId())
                .emvco(emvco)
                .build();
    }

}
