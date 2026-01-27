package com.example.controller;

import com.example.dto.QrRequestDTO;
import com.example.dto.QrValidationRequestDTO;
import com.example.dto.QrValidationResponseDTO;
import com.example.service.QrComparisonService;
import com.example.service.QrEmvcoParserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
public class QrValidationController {

    private final QrEmvcoParserService parserService;
    private final QrComparisonService comparisonService;

    @PostMapping("/validate333")
    public ResponseEntity<QrValidationResponseDTO> validate(
            @RequestBody QrValidationRequestDTO request) {

        QrRequestDTO parsed = parserService.parse(request.getEmvco());

        return ResponseEntity.ok(
                QrValidationResponseDTO.builder()
                        .equals(true)
                        .parsed(parsed)
                        .differences(List.of())
                        .build()
        );
    }
}
