package com.example.webClient;

import com.example.exception.ApiException;
import com.example.util.ErrorCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ThirdPartyQrClient {

    private final WebClient webClient;

    public Mono<Map<String, Object>> parseQr(String qrCode) {

        return webClient.post()
                .uri("/qr/parse")
                .bodyValue(Map.of("qrCode", qrCode))
                .retrieve()
                .onStatus(
                        status -> status.value() == 400,
                        r -> Mono.error(new ApiException(ErrorCatalog.INVALID_CONTENT))
                )
                .onStatus(
                        status -> status.value() == 401,
                        r -> Mono.error(new ApiException(ErrorCatalog.UNAUTHORIZED))
                )
                .onStatus(
                        status -> status.value() == 403,
                        r -> Mono.error(new ApiException(ErrorCatalog.FORBIDDEN))
                )
                .onStatus(
                        status -> status.value() == 502,
                        r -> Mono.error(new ApiException(ErrorCatalog.BAD_GATEWAY))
                )
                .onStatus(
                        status -> status.value() == 503,
                        r -> Mono.error(new ApiException(ErrorCatalog.SERVICE_UNAVAILABLE))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        r -> Mono.error(new ApiException(ErrorCatalog.INTERNAL_ERROR))
                )
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

}
