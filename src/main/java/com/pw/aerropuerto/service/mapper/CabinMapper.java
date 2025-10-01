package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.CabinDtos.*;
import com.pw.aerropuerto.dominio.entities.Cabin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CabinMapper {

    CabinMapper INSTANCE = Mappers.getMapper(CabinMapper.class);


    default Cabin toEntity(CabinCreateRequest request) {
        if (request == null) {
            return null;
        }
        return request.cabin();
    }


    default CabinResponse toResponse(Cabin cabin) {
        if (cabin == null) {
            return null;
        }
        return new CabinResponse(cabin);
    }
}
