package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.FlightDtos.*;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.repositories.FlightRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.FlightMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightServicelmpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public FlightResponse create(FlightCreateRequest flightCreateRequest) {
        Flight flightSave = flightRepository.save(FlightMapper.ToEntity(flightCreateRequest));
        return FlightMapper.ToResponse(flightSave);
    }
    @Override @Transactional(readOnly = true)
    public FlightResponse get(Long id) {
        return flightRepository.findById(id).map(FlightMapper::ToResponse).orElseThrow(()-> new NotFoundException("Flight not found".formatted(id)));

    }

    @Override
    public Page<FlightResponse> list(Pageable pageable){
        return flightRepository.findAll(pageable).map(FlightMapper::ToResponse);

    }
    @Override
    public FlightResponse update(Long id, FlightUpdateRequest req){
        Flight flight = flightRepository.findById(id).orElseThrow(()-> new NotFoundException("Flight not found"));
        if(req.number() != null) flight.setNumber(req.number());
        if(req.departureTime() != null) flight.setDepartureTime(req.departureTime());
        if(req.arrivalTime() != null) flight.setArrivalTime(req.arrivalTime());
        Flight updated = flightRepository.save(flight);
        return FlightMapper.ToResponse(updated);
    }
    @Override
    public void delete(Long id) {
        flightRepository.deleteById(id);
    }
}
