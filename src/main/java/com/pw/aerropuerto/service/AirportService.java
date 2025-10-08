package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.AirportDtos.*;

import java.util.List;

public interface AirportService {
    AirportResponse create(AirportCreateRequest request);
    AirportResponse get(Long id);
    AirportResponse get(String code);
    List<AirportResponse> list();
    List<AirportResponse> list(String code);
    AirportResponse update(Long id,AirportUpdateRequest request);
    void delete(Long id);
}
