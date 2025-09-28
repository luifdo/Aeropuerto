package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.AirlineDtos.*;
import com.pw.aerropuerto.dominio.entities.Airline;
import com.pw.aerropuerto.dominio.repositories.AirlineRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.AirlineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository repository;

    @Override
    public AirlineResponse create(AirlineCreateRequest request) {
        Airline airlineSave = repository.save(AirlineMapper.toEntity(request));
        return AirlineMapper.ToResponse(airlineSave);
    }

    @Override @Transactional(readOnly = true)
    public AirlineResponse get(Long id) {
        return repository.findById(id)
                .map(AirlineMapper::ToResponse)
                .orElseThrow(() -> new NotFoundException("Airline %d not found".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public AirlineResponse getByCode(String code) {
        return repository.findByCode(code)
                .map(AirlineMapper::ToResponse)
                .orElseThrow(() -> new NotFoundException("Airline %s not found".formatted(code)));
    }

    @Override
    public Page<AirlineResponse> lis(Pageable pageable) {
        return repository.findAll(pageable)
                .map(AirlineMapper::ToResponse);
    }

    @Override
    public AirlineResponse update(Long id, AirlineCreateRequest request) {
      Airline airline = repository.findById(id)
              .orElseThrow( () -> new NotFoundException("Airline %d not found".formatted(id)));

      if (request.name() != null) airline.setName(request.name());
      if (request.code() != null) airline.setCode(request.code());

      Airline update = repository.save(airline);
      return AirlineMapper.ToResponse(update);
    }

    @Override
    public void delete(Long id) {
    repository.deleteById(id);
    }
}
