package com.apigateway.users.mapper;

import com.apigateway.users.dto.request.ClientRequestDTO;
import com.apigateway.users.dto.response.ClientResponseDTO;
import com.apigateway.users.model.Client;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:32:40+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client toEntity(ClientRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Client client = new Client();

        client.setActive( dto.active() );
        client.setName( dto.name() );

        return client;
    }

    @Override
    public ClientResponseDTO toDto(Client client) {
        if ( client == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        Boolean active = null;
        LocalDateTime createdAt = null;

        id = client.getId();
        name = client.getName();
        active = client.getActive();
        createdAt = client.getCreatedAt();

        ClientResponseDTO clientResponseDTO = new ClientResponseDTO( id, name, active, createdAt );

        return clientResponseDTO;
    }

    @Override
    public void updateEntity(ClientRequestDTO dto, Client client) {
        if ( dto == null ) {
            return;
        }

        client.setActive( dto.active() );
        client.setName( dto.name() );
    }
}
