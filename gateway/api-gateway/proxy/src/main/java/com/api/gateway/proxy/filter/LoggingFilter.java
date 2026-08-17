package com.api.gateway.proxy.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class LoggingFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        long tiempoInicial = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        var requestModificada = exchange.getRequest()
                .mutate()
                .header("X-Request-ID", requestId)
                .build();

        String metodo = exchange.getRequest()
                .getMethod()
                .name();

        String ruta = exchange.getRequest()
                .getURI()
                .getPath();

        var exchangeModificado = exchange.mutate()
                .request(requestModificada)
                .build();

        System.out.println("Petición recibida");

        return chain.filter(exchangeModificado)
                .then(Mono.fromRunnable(() -> {
                    long duracion = System.currentTimeMillis() - tiempoInicial;
                    System.out.println("[" + requestId + "] "
                                    + "Respuesta: "
                                    + exchange.getResponse()
                                    .getStatusCode());

                    System.out.println("[" + requestId + "] Método: "
                            + metodo + " Ruta: "
                            + ruta);

                    System.out.println("[" + requestId + "] "
                            + "Fecha: "
                            + LocalDateTime.now());

                    System.out.println("[" + requestId + "] "
                                    + "Duración: "
                                    + duracion
                                    + " ms");
                }));
    }
}