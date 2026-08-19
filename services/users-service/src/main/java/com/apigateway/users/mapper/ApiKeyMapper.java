package com.apigateway.users.mapper;


import com.apigateway.users.dto.response.ApiKeyResponseDTO;
import com.apigateway.users.model.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApiKeyMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    ApiKeyResponseDTO toDto(ApiKey apiKey);
}