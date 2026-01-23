package com.example.controller;

import com.example.dto.*;
import com.example.service.QrEmvcoParserService2;
import com.example.service.QrEmvcoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QrController {
    private final QrEmvcoService qrEmvcoService;
    public final QrEmvcoParserService2 qrEmvcoParserService2;

    @PostMapping("/generate")
    public ResponseEntity<QrResponseDTO> generateQr(
            @Valid @RequestBody QrRequestDTO request) {

        return ResponseEntity.ok(
                qrEmvcoService.generateEmvco(request)
        );
    }

    @PostMapping("/parse")
    public ResponseEntity<QrEmvcoResponseDTO> parse(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(
                qrEmvcoParserService2.parse(request.get("emvco"))
        );
    }

    @PostMapping("/parse")
    public ResponseEntity<QrResponseParseDTO> parse(@RequestBody QrRequestParseDto request) {
        return ResponseEntity.ok(qrEmvcoParserService2.parse2(request));
    }
}
