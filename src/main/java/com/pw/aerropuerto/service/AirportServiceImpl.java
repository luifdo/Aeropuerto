package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.AirportDtos;
import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.repositories.AirportRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {
    private final AirportRepository repository;


    @Override
    public AirportDtos.AirportResponse create(AirportDtos.AirportCreateRequest request) {
        Airport airportSave = repository.save(AirportMapper.ToEntity(request));
        return AirportMapper.ToResponse(airportSave);
    }

    @Override @Transactional(readOnly = true)
    public AirportDtos.AirportResponse get(Long id) {
        return repository.findById(id)
                .map(AirportMapper::ToResponse)
                .orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(id)));

    }

    @Override @Transactional(readOnly = true)
    public AirportDtos.AirportResponse get(String code) {
        return repository.findByCode(code)
                .map(AirportMapper::ToResponse)
                .orElseThrow(() -> new NotFoundException("Airport %s not found".formatted(code)));
    }

    @Override @Transactional(readOnly = true)
    public List<AirportDtos.AirportResponse> list() {
        return repository.findAll()
                .stream().map(AirportMapper::ToResponse)
                .collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<AirportDtos.AirportResponse> list(String code) {
        return repository.findAll()
                .stream().map(AirportMapper::ToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AirportDtos.AirportResponse update(Long id, AirportDtos.AirportUpdateRequest request) {
        var u = repository.findById(id).orElseThrow(() -> new NotFoundException("Airport %d not found".formatted(id)));

        AirportMapper.path(u, request);

        return AirportMapper.ToResponse(u);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);

    }
}
