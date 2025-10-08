package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.BookingDtos.BookingCreateRequest;
import com.pw.aerropuerto.api.dto.BookingDtos.BookingUpdateRequest;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Booking;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.repositories.BookingRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServicelmplTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServicelmpl service; // usa exactamente el nombre de la clase del proyecto

    @Test
    void create_debe_guardar_y_devolver_response() {
        Booking booking = mock(Booking.class);
        BookingCreateRequest req = mock(BookingCreateRequest.class);
        Passenger passenger = new Passenger(); // si no tiene required fields, ok
        List<BookItem> items = List.of();

        Booking saved = Booking.builder()
                .id(11L)
                .items(new ArrayList<>())
                // setea sólo lo mínimo necesario para las assertions; usa los campos que tu mapper/toResponse expone
                .build();

        when(bookingRepository.save(any(Booking.class))).thenReturn(saved);

        var resp = service.create(req, passenger, items);

        assertThat(resp).isNotNull();
        // asumo que el response tiene un id() o getId() que el mapper rellena; ajusta si tu DTO usa otro nombre
        assertThat(resp.id()).isEqualTo(11L);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void get_debe_devolver_response_si_existe() {
        Booking booking = Booking.builder().id(5L).build();

        when(bookingRepository.findById(5L)).thenReturn(Optional.of(booking));

        var resp = service.get(5L);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(5L);
    }

    @Test
    void get_debe_lanzar_NotFoundException_si_no_existe() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    void list_debe_devolver_pagina_de_responses() {
        Booking a = Booking.builder().id(1L).build();
        Booking b = Booking.builder().id(2L).build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Booking> page = new PageImpl<>(List.of(a, b), pageable, 2);

        when(bookingRepository.findAll(pageable)).thenReturn(page);

        Page<?> respPage = service.list(pageable);

        assertThat(respPage).isNotNull();
        assertThat(respPage.getTotalElements()).isEqualTo(2);
        assertThat(respPage.getContent()).hasSize(2);
    }

    @Test
    void update_debe_guardar_aun_si_request_no_modifica_campos() {
        Long id = 20L;
        Booking existing = Booking.builder()
                .id(id)
                .build();

        when(bookingRepository.findById(id)).thenReturn(Optional.of(existing));

        BookingUpdateRequest req = mock(BookingUpdateRequest.class);
        // hacemos que todos los getters usados en update retornen null para no intentar asignar tipos desconocidos
        when(req.updatedAt()).thenReturn(null);
        when(req.passengerId()).thenReturn(null);
        when(req.bookItemIds()).thenReturn(null);

        Booking afterSave = Booking.builder().id(id).build();
        when(bookingRepository.save(any(Booking.class))).thenReturn(afterSave);

        var resp = service.update(id, req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(id);

        verify(bookingRepository).findById(id);
        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(captor.capture());
        Booking savedArg = captor.getValue();
        assertThat(savedArg.getId()).isEqualTo(id);
    }

    @Test
    void update_debe_lanzar_NotFoundException_si_no_existe() {
        Long id = 300L;
        when(bookingRepository.findById(id)).thenReturn(Optional.empty());

        BookingUpdateRequest req = mock(BookingUpdateRequest.class);
        when(req.updatedAt()).thenReturn(null);
        when(req.passengerId()).thenReturn(null);
        when(req.bookItemIds()).thenReturn(null);

        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    void delete_debe_eliminar_por_id() {
        Long id = 7L;
        doNothing().when(bookingRepository).deleteById(id);

        service.delete(id);

        verify(bookingRepository, times(1)).deleteById(id);
    }
}
