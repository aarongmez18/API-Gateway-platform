package com.apigateway.api.mapper;

import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.model.Api;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ApiMapper {

    Api toEntity(ApiRequestDTO dto);

    ApiResponseDTO toDto(Api api);

    void updateEntity(ApiRequestDTO dto, @MappingTarget Api api);
}