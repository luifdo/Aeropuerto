package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.FlightDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightService {
    FlightResponse create(FlightCreateRequest req);
    FlightResponse get(Long id);
    Page<FlightResponse> list(Pageable pageable);
    FlightResponse update(Long id, FlightUpdateRequest req);
    void delete(Long id);
}
