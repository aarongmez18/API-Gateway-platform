package com.api.gateway.proxy.client;

import com.api.gateway.proxy.dto.RequestLogRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;

@Component
public class RequestLogClient {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLogClient.class);
    private final WebClient webClient;

    public RequestLogClient(WebClient.Builder builder, @Value("${services.requests.url:http://localhost:8085}") String requestsServiceUrl) { this.webClient = builder.baseUrl(requestsServiceUrl).build(); }

    public void create(RequestLogRequestDTO dto) {
        webClient.post()
                .uri("/requests-management/internal/request-logs")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(ex -> LOG.warn("No se pudo registrar petición -- apiCode={} -- endpoint={} -- error={}", dto.apiCode(), dto.endpoint(), ex.getMessage()))
                .onErrorResume(ex -> Mono.empty())
                .subscribe();
    }
}