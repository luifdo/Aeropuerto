package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.PassangerDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PassengerService {
    PassengerResponse create(PassengerCreateRequest request);
    PassengerResponse get(Long id);
    PassengerResponse getByEmail(String email);
    Page<PassengerResponse> lis(Pageable pageable);
    PassengerResponse update(Long id, PassengerUpdateRequest request);
    void delete(Long id);
}
