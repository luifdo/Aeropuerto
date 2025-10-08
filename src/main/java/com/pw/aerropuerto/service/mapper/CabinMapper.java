package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.CabinDtos;
import com.pw.aerropuerto.dominio.entities.Cabin;

public class CabinMapper {
    public static Cabin ToEntity(CabinDtos.CabinCreateRequest request){
        return request.cabin();
    }
    public static CabinDtos.CabinResponse ToResponse(Cabin cabin){
        return new CabinDtos.CabinResponse(cabin);
    }
}
