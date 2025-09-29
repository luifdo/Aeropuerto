package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.BookingDtos.*;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.repositories.BookingRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServicelmpl implements BookingService {

    private final BookingRepository bookingRepository;
    @Override
    public BookingResponse create(BookingCreateRequest request, Passenger passenger, List<BookItem> items) {
        Booking bookingsave = bookingRepository.save(BookingMapper.ToEntity(request, passenger, items));
        return BookingMapper.toResponse(bookingsave);
    }
    @Override @Transactional(readOnly = true)
    public BookingResponse get(Long id) {
        return bookingRepository.findById(id).map(BookingMapper::toResponse).orElseThrow(()-> new NotFoundException("Booking not found".formatted(id)));
    }
    @Override
    public Page<BookingResponse> list(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(BookingMapper::toResponse);
    }
    @Override
    public BookingResponse update(Long id,BookingUpdateRequest request) {
        Booking booking = bookingRepository.findById(id).orElseThrow(()-> new NotFoundException("Booking not found"));
        if (request.updatedAt() != null) booking.setCreatedAt(request.updatedAt());
        if (request.passengerId()  != null)booking.setPassenger(request.passengerId());
        if (request.bookItemIds() != null) booking.setItems(request.bookItemIds());

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toResponse(updatedBooking);
    }
    @Override
    public void delete(Long id) {
        bookingRepository.deleteById(id);
    }
}
