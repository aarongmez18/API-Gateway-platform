package com.api.gateway.proxy.filter;

import com.api.gateway.proxy.client.RequestLogClient;
import com.api.gateway.proxy.dto.RequestLogRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class RequestLoggingGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestLoggingGatewayFilterFactory.Config> {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLoggingGatewayFilterFactory.class);
    private final RequestLogClient requestLogClient;

    public RequestLoggingGatewayFilterFactory(RequestLogClient requestLogClient) {
        super(Config.class);
        this.requestLogClient = requestLogClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            long startedAt = System.nanoTime();
            OffsetDateTime requestedAt = OffsetDateTime.now();

            return chain.filter(exchange).doFinally(signalType -> {
                long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startedAt);
                HttpStatusCode status = exchange.getResponse().getStatusCode();
                int statusCode = status != null ? status.value() : 500;

                Long clientId = exchange.getAttribute(ApiKeyAuthenticationGatewayFilterFactory.AUTHENTICATED_CLIENT_ID_ATTR);
                String clientName = exchange.getAttribute(ApiKeyAuthenticationGatewayFilterFactory.AUTHENTICATED_CLIENT_NAME_ATTR);
                String endpoint = exchange.getRequest().getURI().getRawPath();
                String method = exchange.getRequest().getMethod().name();

                RequestLogRequestDTO dto = new RequestLogRequestDTO(clientId, clientName, config.getApiCode(), endpoint, method, statusCode, durationMs, requestedAt);

                LOG.info("REQUEST -- clientId={} -- apiCode={} -- method={} -- endpoint={} -- status={} -- duration={}ms", clientId, config.getApiCode(), method, endpoint, statusCode, durationMs);

                requestLogClient.create(dto);
            });
        };
    }

    public static class Config {
        private String apiCode;

        public String getApiCode() { return apiCode; }
        public void setApiCode(String apiCode) { this.apiCode = apiCode; }
    }
}