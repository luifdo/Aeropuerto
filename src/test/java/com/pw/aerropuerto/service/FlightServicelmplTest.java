package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.FlightDtos.FlightCreateRequest;
import com.pw.aerropuerto.api.dto.FlightDtos.FlightUpdateRequest;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.repositories.FlightRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServicelmplTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServicelmpl service; // usa exactamente el nombre de la clase bajo prueba

    @Test
    void create_debe_guardar_y_devolver_response() {
        FlightCreateRequest req = mock(FlightCreateRequest.class);
        ZoneOffset offset = ZoneOffset.ofHours(-5);

        OffsetDateTime departure = OffsetDateTime.of(LocalDateTime.of(2025, 10, 1, 8, 0), offset);
        OffsetDateTime arrival = OffsetDateTime.of(LocalDateTime.of(2025, 10, 1, 12, 0), offset);

        when(req.number()).thenReturn("FL-100");
        lenient().when(req.departureTime()).thenReturn(departure); // esto como tal no es necesario pero no entiendo pq
        lenient().when(req.arrivalTime()).thenReturn(arrival);


        Flight saved = Flight.builder()
                .id(99L)
                .number("FL-100")
                .departureTime(departure)
                .arrivalTime(arrival)
                .build();

        when(flightRepository.save(any(Flight.class))).thenReturn(saved);

        var resp = service.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(99L);
        assertThat(resp.number()).isEqualTo("FL-100");
        assertThat(resp.departureTime()).isEqualTo(departure);
        assertThat(resp.arrivalTime()).isEqualTo(arrival);


        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void get_debe_devolver_response_si_existe() {
        ZoneOffset offset = ZoneOffset.ofHours(-5);

        OffsetDateTime departure = OffsetDateTime.of(LocalDateTime.of(2025, 11, 1, 9, 0), offset);
        OffsetDateTime arrival = OffsetDateTime.of(LocalDateTime.of(2025, 11, 1, 13, 0), offset);

        Flight f = Flight.builder()
                .id(5L)
                .number("FL-5")
                .departureTime(departure)
                .arrivalTime(arrival)
                .build();

        when(flightRepository.findById(5L)).thenReturn(Optional.of(f));

        var resp = service.get(5L);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(5L);
        assertThat(resp.number()).isEqualTo("FL-5");
    }

    @Test
    void get_debe_lanzar_NotFoundException_si_no_existe() {
        when(flightRepository.findById(123L)).thenReturn(Optional.empty());
        // en el anterior de booking tenias las vainas de asegurarse que mande null y aca no descarado
        // o sea este si lo hiciste bien

        assertThatThrownBy(() -> service.get(123L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Flight not found");
    }

    @Test
    void list_debe_devolver_pagina_de_responses() {
        Flight a = Flight.builder().id(1L).number("A1").build();
        Flight b = Flight.builder().id(2L).number("B2").build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Flight> page = new PageImpl<>(List.of(a, b), pageable, 2);

        when(flightRepository.findAll(pageable)).thenReturn(page);

        Page<?> respPage = service.list(pageable);

        assertThat(respPage).isNotNull();
        assertThat(respPage.getTotalElements()).isEqualTo(2);
        assertThat(respPage.getContent()).hasSize(2);
    }

    @Test
    void update_debe_modificar_campos_y_guardar_si_existe() {
        Long id = 20L;
        ZoneOffset offset = ZoneOffset.ofHours(-5);

        OffsetDateTime departureOld = OffsetDateTime.of(LocalDateTime.of(2025, 12, 1, 6, 0), offset);
        OffsetDateTime arrivalOld = OffsetDateTime.of(LocalDateTime.of(2025, 12, 1, 10, 0), offset);

        Flight existing = Flight.builder()
                .id(id)
                .number("OLD-100")
                .departureTime(departureOld)
                .arrivalTime(arrivalOld)
                .build();

        when(flightRepository.findById(id)).thenReturn(Optional.of(existing));

        OffsetDateTime departureNew = OffsetDateTime.of(LocalDateTime.of(2025, 12, 2, 7, 0), offset);
        OffsetDateTime arrivalNew = OffsetDateTime.of(LocalDateTime.of(2025, 12, 2, 11, 0), offset);

        FlightUpdateRequest req = mock(FlightUpdateRequest.class);
        when(req.number()).thenReturn("NEW-200");
        when(req.departureTime()).thenReturn(departureNew);
        when(req.arrivalTime()).thenReturn(arrivalNew);

        Flight updatedEntity = Flight.builder()
                .id(id)
                .number("NEW-200")
                .departureTime(departureNew)
                .arrivalTime(arrivalNew)
                .build();

        when(flightRepository.save(any(Flight.class))).thenReturn(updatedEntity);

        var resp = service.update(id, req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(id);
        assertThat(resp.number()).isEqualTo("NEW-200");
        assertThat(resp.departureTime()).isEqualTo(OffsetDateTime.of(2025, 12, 2, 7, 0,0,0,offset));
        assertThat(resp.arrivalTime()).isEqualTo(OffsetDateTime.of(2025, 12, 2, 11, 0,0,0,offset));

        verify(flightRepository).findById(id);
        ArgumentCaptor<Flight> captor = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository).save(captor.capture());
        Flight savedArg = captor.getValue();
        assertThat(savedArg.getNumber()).isEqualTo("NEW-200");
        assertThat(savedArg.getDepartureTime()).isEqualTo(OffsetDateTime.of(2025, 12, 2, 7, 0, 0, 0, offset));
        assertThat(savedArg.getArrivalTime()).isEqualTo(OffsetDateTime.of(2025, 12, 2, 11, 0, 0, 0,offset));
    }

    @Test
    void update_debe_lanzar_NotFoundException_si_no_existe() {
        Long id = 777L;
        when(flightRepository.findById(id)).thenReturn(Optional.empty());

        FlightUpdateRequest req = mock(FlightUpdateRequest.class);
//        when(req.number()).thenReturn(null); otra vez  arriba lo hiciste bien y aca mal mas raro...
//        when(req.departureTime()).thenReturn(null);
//        when(req.arrivalTime()).thenReturn(null);

        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Flight not found");
    }

    @Test
    void delete_debe_eliminar_por_id() {
        Long id = 7L;
        doNothing().when(flightRepository).deleteById(id);

        service.delete(id);

        verify(flightRepository, times(1)).deleteById(id);
    }
}
