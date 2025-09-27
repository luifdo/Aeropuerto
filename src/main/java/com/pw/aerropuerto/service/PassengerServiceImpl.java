package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.PassangerDtos.*;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.repositories.PassengerRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.PassengerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository repository;

    @Override public PassengerResponse create(PassengerCreateRequest request) {
        Passenger passengerSaved = repository.save(PassengerMapper.ToEntity(request));
        return PassengerMapper.toResponse(passengerSaved);
    }

    @Override @Transactional
    public PassengerResponse get(Long id) {
        return  repository.findById(id)
                .map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(id)));

    }

    @Override @Transactional
    public PassengerResponse getByEmail(String email) {
        return repository.findByEmailIgnoreCaseQuery(email)
                .map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger %s not found".formatted(email)));
    }

    @Override @Transactional
    public Page<PassengerResponse> lis(Pageable pageable) {
        return repository.findAll(pageable).map(PassengerMapper::toResponse);
    }


    @Override
    public PassengerResponse update(Long id, PassengerCreateRequest request) {
        var m = repository.findById(id).orElseThrow(() -> new NotFoundException("Passenger %d not found".formatted(id)));
        PassengerMapper.path(m,request);
        return PassengerMapper.toResponse(m);
    }

    @Override
    public void delete(Long id) { repository.deleteById(id); }

    }




