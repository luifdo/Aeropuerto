package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.BookingDtos.*;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {
BookingResponse create(BookingCreateRequest req, Passenger passenger, List<BookItem> items);
BookingResponse get(Long id);
Page<BookingResponse> list(Pageable pageable);
BookingResponse update(Long id, BookingUpdateRequest req);
void delete(Long id);
}
