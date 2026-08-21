package com.apigateway.users.mapper;

import com.apigateway.users.dto.response.ApiKeyResponseDTO;
import com.apigateway.users.model.ApiKey;
import com.apigateway.users.model.Client;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T17:11:36+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ApiKeyMapperImpl implements ApiKeyMapper {

    @Override
    public ApiKeyResponseDTO toDto(ApiKey apiKey) {
        if ( apiKey == null ) {
            return null;
        }

        Long clientId = null;
        String clientName = null;
        Long id = null;
        Boolean active = null;
        LocalDateTime createdAt = null;

        clientId = apiKeyClientId( apiKey );
        clientName = apiKeyClientName( apiKey );
        id = apiKey.getId();
        active = apiKey.getActive();
        createdAt = apiKey.getCreatedAt();

        ApiKeyResponseDTO apiKeyResponseDTO = new ApiKeyResponseDTO( id, clientId, clientName, active, createdAt );

        return apiKeyResponseDTO;
    }

    private Long apiKeyClientId(ApiKey apiKey) {
        Client client = apiKey.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getId();
    }

    private String apiKeyClientName(ApiKey apiKey) {
        Client client = apiKey.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getName();
    }
}
