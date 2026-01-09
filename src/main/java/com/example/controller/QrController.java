package com.example.controller;

import com.example.dto.QrRequestDTO;
import com.example.dto.QrResponseDTO;
import com.example.service.QrEmvcoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QrController {
    private final QrEmvcoService qrEmvcoService;

    @PostMapping("/generate")
    public ResponseEntity<QrResponseDTO> generateQr(
            @Valid @RequestBody QrRequestDTO request) {

        return ResponseEntity.ok(
                qrEmvcoService.generateEmvco(request)
        );
    }
}
