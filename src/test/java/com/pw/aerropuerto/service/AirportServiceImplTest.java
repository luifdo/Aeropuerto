package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.AirportDtos.*;
import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.repositories.AirportRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {

    @Mock
    private AirportRepository repository;

    @InjectMocks
    private AirportServiceImpl service;

    private Airport airport;

    @BeforeEach
    void setUp() {
        airport = Airport.builder()
                .id(1L)
                .Name("El Dorado")
                .Code("BOG")
                .City("Bogotá")
                .build();
    }

    @Test
    void create_ShouldReturnResponse() {
        AirportCreateRequest request = new AirportCreateRequest("El Dorado", "BOG", "Bogotá");
        when(repository.save(any(Airport.class))).thenReturn(airport);

        AirportResponse response = service.create(request);

        assertThat(response).isNotNull();
        assertThat(response.Name()).isEqualTo("El Dorado");
        assertThat(response.Code()).isEqualTo("BOG");
        assertThat(response.City()).isEqualTo("Bogotá");
        verify(repository).save(any(Airport.class));
    }

    @Test
    void getById_ShouldReturnResponse_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(airport));

        AirportResponse response = service.get(1L);

        assertThat(response).isNotNull();
        assertThat(response.Code()).isEqualTo("BOG");
        assertThat(response.Name()).isEqualTo("El Dorado");
    }

    @Test
    void getById_ShouldThrow_WhenNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airport 99 not found");
    }

    @Test
    void getByCode_ShouldReturnResponse_WhenExists() {
        when(repository.findByCode("BOG")).thenReturn(Optional.of(airport));

        AirportResponse response = service.get("BOG");

        assertThat(response).isNotNull();
        assertThat(response.Name()).isEqualTo("El Dorado");
        assertThat(response.City()).isEqualTo("Bogotá");
    }

    @Test
    void getByCode_ShouldThrow_WhenNotExists() {
        when(repository.findByCode("XXX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get("XXX"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airport XXX not found");
    }

    @Test
    void list_ShouldReturnResponses() {
        when(repository.findAll()).thenReturn(List.of(airport));

        List<AirportResponse> result = service.list();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).Code()).isEqualTo("BOG");
        assertThat(result.get(0).Name()).isEqualTo("El Dorado");
    }

    @Test
    void listWithCode_ShouldReturnResponses() {
        when(repository.findAll()).thenReturn(List.of(airport));

        List<AirportResponse> result = service.list("BOG");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).Name()).isEqualTo("El Dorado");
    }

    @Test
    void update_ShouldModifyAndReturnResponse() {
        AirportUpdateRequest request = new AirportUpdateRequest("New Name", "NEW", "Bogotá");
        when(repository.findById(1L)).thenReturn(Optional.of(airport));

        AirportResponse response = service.update(1L, request);

        assertThat(response).isNotNull();
        assertThat(response.Code()).isEqualTo("New Name");
        assertThat(response.Name()).isEqualTo("NEW");
        assertThat(response.City()).isEqualTo("Bogotá");
    }

    @Test
    void update_ShouldThrow_WhenNotExists() {
        AirportUpdateRequest request = new AirportUpdateRequest("New Name", "NEW", "CityX");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airport 99 not found");
    }

    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}
