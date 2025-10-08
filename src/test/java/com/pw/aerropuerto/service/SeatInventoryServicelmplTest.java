package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.SeatInventoryDtos.SeatInventoryRequest;
import com.pw.aerropuerto.api.dto.SeatInventoryDtos.SeatInventoryResponse;
import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.entities.Flight;
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

        Flight flight = Flight.builder().id(100l).build();
        SeatInventoryRequest req = new SeatInventoryRequest(Cabin.ECONOMY,20,flight);
        SeatInventory saved = SeatInventory.builder()
                .id(1L)
                .Cabin(Cabin.ECONOMY)
                .availableSeats(20)
                .totalSeats(20)
                .flight(flight)
                .build();

        when(repository.save(any(SeatInventory.class))).thenReturn(saved);

        SeatInventoryResponse resp = service.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(1L);
        verify(repository, times(1)).save(any(SeatInventory.class));
    }

    @Test
    void get_debe_devolver_response_si_existe() {
        SeatInventory seat = SeatInventory.builder()
                .id(5L)
                .Cabin(Cabin.ECONOMY)
                .availableSeats(20)
                .totalSeats(20)
                .flight(Flight.builder().id(100l).build())
                .build();
        // aca no servia pq no le estas mandando todo del searInventory es decir no ponias ni la cabina ni los asientos ni el vuelo
        // tienes muchos errores asi
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
        SeatInventory a = SeatInventory.builder()
                .id(1L)
                .Cabin(Cabin.ECONOMY)
                .availableSeats(20)
                .totalSeats(20)
                .flight(Flight.builder().id(100l).build())
                .build();
        SeatInventory b = SeatInventory.builder()
                .id(1L).Cabin(Cabin.ECONOMY)
                .availableSeats(20)
                .totalSeats(20)
                .flight(Flight.builder().id(100l).build())
                .build();

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
        SeatInventory existing = SeatInventory.builder()
                .id(id)
                .Cabin(Cabin.ECONOMY)
                .availableSeats(100)
                .totalSeats(100)
                .flight(Flight.builder().id(100l).build())
                .build();
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        SeatInventoryRequest req = mock(SeatInventoryRequest.class);
        when(req.cabin()).thenReturn(Cabin.BUSINESS);
        when(req.availableSeats()).thenReturn(50);
        when(req.flightId()).thenReturn(null);

        SeatInventory updated = SeatInventory.builder()
                .id(id)
                .Cabin(Cabin.BUSINESS)
                .availableSeats(50)
                .totalSeats(50)
                .flight(Flight.builder().id(100l).build())
                .build();
        when(repository.save(any(SeatInventory.class))).thenReturn(updated);

        SeatInventoryResponse resp = service.update(id, req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(id);
        verify(repository).findById(id);

        ArgumentCaptor<SeatInventory> captor = ArgumentCaptor.forClass(SeatInventory.class);
        verify(repository).save(captor.capture());
        assertThat(captor.getValue().getCabin()).isEqualByComparingTo(Cabin.BUSINESS);
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
