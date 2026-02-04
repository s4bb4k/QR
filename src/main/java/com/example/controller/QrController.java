package com.example.controller;

import com.example.dto.*;
import com.example.service.QrEmvcoParserService2;
import com.example.service.QrEmvcoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/qr")
@RequiredArgsConstructor
@Slf4j
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
    public ResponseEntity<?> parse(@RequestBody Map<String, String> request) {
        log.info("Received request {}", request);
        ResponseEntity<?> response = qrEmvcoParserService2.parse(request.get("emvco"));
        /*return ResponseEntity.ok(
                qrEmvcoParserService2.parse(request.get("emvco"))
        );*/
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/parse2")
    public ResponseEntity<QrResponseParseDTO> parse(@RequestBody QrRequestParseDto request) {
        return ResponseEntity.ok(qrEmvcoParserService2.parse2(request));
    }

    @PostMapping("/generate2")
    public ResponseEntity<QrGenerateResponseDTO> generateQr(
            @RequestBody QrGenerateRequestDTO request) {

        QrGenerateResponseDTO response = qrEmvcoParserService2.generate(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<QrValidateResponseDTO> validate(
            @RequestBody QrValidateRequestDTO request) {

        QrValidateResponseDTO response = qrEmvcoParserService2.validate(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<QrUpdateStatusResponseDTO> updateStatus(
            @PathVariable("id") String qrId,
            @RequestBody QrUpdateStatusRequestDTO request) {

        QrUpdateStatusResponseDTO response =
                qrEmvcoParserService2.updateStatus(qrId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QrQueryResponseDTO> query(
            @PathVariable("id") String qrId) {

        QrQueryResponseDTO response = qrEmvcoParserService2.query(qrId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parse3")
    public ResponseEntity<?>  parse(@Valid @RequestBody EmvQrRequest request) {

        ResponseEntity<?> response = qrEmvcoParserService2.parse3(request.qr());
        return ResponseEntity.ok(response.getBody());
        /*return new EmvQrResponse(
                payload.isCrcValid(),
                payload.getCrcValueHex(),
                payload.getMultillaveBreB(),
                payload.getFlat()
        );*/
    }


}
