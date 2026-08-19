package com.apigateway.users.mapper;

import com.apigateway.users.dto.request.ClientRequestDTO;
import com.apigateway.users.dto.response.ClientResponseDTO;
import com.apigateway.users.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientRequestDTO dto);
    ClientResponseDTO toDto(Client client);
    void updateEntity(ClientRequestDTO dto, @MappingTarget Client client);
}