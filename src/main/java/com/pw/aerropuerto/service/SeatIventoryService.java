package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.SeatInventoryDtos.*;
import com.pw.aerropuerto.dominio.entities.Cabin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SeatIventoryService {
    SeatInventoryResponse create(SeatInventoryRequest req);
    SeatInventoryResponse get(Long id);
    Page<SeatInventoryResponse> list(Pageable pageable);
    SeatInventoryResponse update(Long id, SeatInventoryRequest req);
    void delete(Long id);
}
