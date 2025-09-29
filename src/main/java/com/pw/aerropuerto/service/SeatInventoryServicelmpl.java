package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.SeatInventoryDtos.*;
import com.pw.aerropuerto.dominio.entities.SeatInventory;
import com.pw.aerropuerto.dominio.repositories.SeatInventoryRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.SeatInventoryMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatInventoryServicelmpl implements SeatInventoryService {
    private final SeatInventoryRepository seatInventoryRepository;

    @Override
    public SeatInventoryResponse create(SeatInventoryRequest req) {
        SeatInventory seatInventorySave = seatInventoryRepository.save(SeatInventoryMapper.ToEntity(req));
        return SeatInventoryMapper.ToResponse(seatInventorySave);
    }
    @Override @Transactional(readOnly = true)
    public SeatInventoryResponse get(Long id) {
        return seatInventoryRepository.findById(id).map(SeatInventoryMapper::ToResponse).orElseThrow(()-> new NotFoundException("SeatInventory not found".formatted(id)));
    }

    @Override
    public Page<SeatInventoryResponse> list(Pageable pageable) {
        return seatInventoryRepository.findAll(pageable).map(SeatInventoryMapper::ToResponse);
    }
    @Override
    public SeatInventoryResponse update(Long id, SeatInventoryRequest req){
        SeatInventory seatInventory = seatInventoryRepository.findById(id).orElseThrow(()-> new NotFoundException("SeatInventory not found".formatted(id)));
        if(req.cabin()!=null) seatInventory.setCabin(req.cabin());
        if(req.availableSeats()!=null) seatInventory.setAvailableSeats(req.availableSeats());
        if(req.flightId()!=null)seatInventory.setFlight(req.flightId());

        SeatInventory seatInventoryUpdate = seatInventoryRepository.save(seatInventory);
        return SeatInventoryMapper.ToResponse(seatInventoryUpdate);
    }
    @Override
    public void delete(Long id) {
        seatInventoryRepository.deleteById(id);
    }
}
