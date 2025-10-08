package com.pw.aerropuerto.service;


import com.pw.aerropuerto.api.dto.AirlineDtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AirlineService {

    AirlineResponse create(AirlineCreateRequest request);
    AirlineResponse get(Long id);
    AirlineResponse getByCode(String code);
    Page<AirlineResponse> lis(Pageable pageable);
    AirlineResponse update(Long id, AirlineUpdateRequest request);
    void delete(Long id);


}
