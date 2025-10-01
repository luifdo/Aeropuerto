package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.SeatInventoryDtos.SeatInventoryRequest;
import com.pw.aerropuerto.api.dto.SeatInventoryDtos.SeatInventoryResponse;
import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.entities.SeatInventory;
import com.pw.aerropuerto.dominio.repositories.SeatInventoryRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatInventoryServicelmplTest {

    @Mock
    private SeatInventoryRepository repository;

    @InjectMocks
    private SeatInventoryServicelmpl service;

    @Test
    void create_debe_guardar_y_devolver_response() {
        SeatInventoryRequest req = mock(SeatInventoryRequest.class);
        SeatInventory saved = SeatInventory.builder().id(1L).build();

        when(repository.save(any(SeatInventory.class))).thenReturn(saved);

        SeatInventoryResponse resp = service.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(1L);
        verify(repository, times(1)).save(any(SeatInventory.class));
    }

    @Test
    void get_debe_devolver_response_si_existe() {
        SeatInventory seat = SeatInventory.builder().id(5L).build();
        when(repository.findById(5L)).thenReturn(Optional.of(seat));

        SeatInventoryResponse resp = service.get(5L);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(5L);
    }

    @Test
    void get_debe_lanzar_NotFoundException_si_no_existe() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("SeatInventory not found");
    }

    @Test
    void list_debe_devolver_pagina_de_responses() {
        SeatInventory a = SeatInventory.builder().id(1L).build();
        SeatInventory b = SeatInventory.builder().id(2L).build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<SeatInventory> page = new PageImpl<>(List.of(a, b), pageable, 2);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<SeatInventoryResponse> respPage = service.list(pageable);

        assertThat(respPage).isNotNull();
        assertThat(respPage.getTotalElements()).isEqualTo(2);
        assertThat(respPage.getContent()).hasSize(2);
    }

    @Test
    void update_debe_guardar_cuando_existe() {
        Long id = 10L;
        SeatInventory existing = SeatInventory.builder().id(id).Cabin(Cabin.ECONOMY).availableSeats(100).build();
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        SeatInventoryRequest req = mock(SeatInventoryRequest.class);
        when(req.cabin()).thenReturn(Cabin.BUSINESS);
        when(req.availableSeats()).thenReturn(50);
        when(req.flightId()).thenReturn(null);

        SeatInventory updated = SeatInventory.builder().id(id).Cabin(Cabin.BUSINESS).availableSeats(50).build();
        when(repository.save(any(SeatInventory.class))).thenReturn(updated);

        SeatInventoryResponse resp = service.update(id, req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(id);
        verify(repository).findById(id);

        ArgumentCaptor<SeatInventory> captor = ArgumentCaptor.forClass(SeatInventory.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getCabin()).isEqualTo("BUSINESS");
        assertThat(captor.getValue().getAvailableSeats()).isEqualTo(50);
    }

    @Test
    void update_debe_lanzar_NotFoundException_si_no_existe() {
        when(repository.findById(77L)).thenReturn(Optional.empty());
        SeatInventoryRequest req = mock(SeatInventoryRequest.class);

        assertThatThrownBy(() -> service.update(77L, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("SeatInventory not found");
    }

    @Test
    void delete_debe_eliminar_por_id() {
        Long id = 7L;
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
