package com.apigateway.api.mapper;

import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.model.Api;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:54:32+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class ApiMapperImpl implements ApiMapper {

    @Override
    public Api toEntity(ApiRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Api api = new Api();

        api.setActive( dto.getActive() );
        api.setTargetUrl( dto.getTargetUrl() );
        api.setPath( dto.getPath() );
        api.setName( dto.getName() );

        return api;
    }

    @Override
    public ApiResponseDTO toDto(Api api) {
        if ( api == null ) {
            return null;
        }

        ApiResponseDTO apiResponseDTO = new ApiResponseDTO();

        apiResponseDTO.setId( api.getId() );
        apiResponseDTO.setName( api.getName() );
        apiResponseDTO.setTargetUrl( api.getTargetUrl() );
        apiResponseDTO.setPath( api.getPath() );
        apiResponseDTO.setActive( api.getActive() );
        apiResponseDTO.setCreatedAt( api.getCreatedAt() );

        return apiResponseDTO;
    }

    @Override
    public void updateEntity(ApiRequestDTO dto, Api api) {
        if ( dto == null ) {
            return;
        }

        api.setActive( dto.getActive() );
        api.setTargetUrl( dto.getTargetUrl() );
        api.setPath( dto.getPath() );
        api.setName( dto.getName() );
    }
}
