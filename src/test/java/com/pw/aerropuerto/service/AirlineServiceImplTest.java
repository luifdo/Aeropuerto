package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.AirlineDtos.*;
import com.pw.aerropuerto.dominio.entities.Airline;
import com.pw.aerropuerto.dominio.repositories.AirlineRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplTest {

    @Mock
    private AirlineRepository repository;

    @InjectMocks
    private AirlineServiceImpl service;

    private Airline airline;

    @BeforeEach
    void setUp() {
        airline = Airline.builder()
                .id(1L)
                .name("Avianca")
                .code("AV")
                .build();
    }

    @Test
    void create_ShouldReturnResponse() {
        AirlineCreateRequest request = new AirlineCreateRequest("Avianca", "AV");

        when(repository.save(any(Airline.class))).thenReturn(airline);

        AirlineResponse response = service.create(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Avianca");
        assertThat(response.code()).isEqualTo("AV");
        verify(repository).save(any(Airline.class));
    }

    @Test
    void get_ShouldReturnResponse_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(airline));

        AirlineResponse response = service.get(1L);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Avianca");
    }

    @Test
    void get_ShouldThrow_WhenNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airline 99 not found");
    }

    @Test
    void getByCode_ShouldReturnResponse_WhenExists() {
        when(repository.findByCode("AV")).thenReturn(Optional.of(airline));

        AirlineResponse response = service.getByCode("AV");

        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo("AV");
    }

    @Test
    void getByCode_ShouldThrow_WhenNotExists() {
        when(repository.findByCode("XX")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByCode("XX"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airline XX not found");
    }

    @Test
    void list_ShouldReturnPagedResponses() {
        Page<Airline> page = new PageImpl<>(List.of(airline));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<AirlineResponse> result = service.lis(Pageable.unpaged());

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Avianca");
    }

    @Test
    void update_ShouldModifyAndReturnResponse() {
        AirlineUpdateRequest request = new AirlineUpdateRequest("Latam", "LA");
        when(repository.findById(1L)).thenReturn(Optional.of(airline));
        when(repository.save(any(Airline.class))).thenReturn(
                Airline.builder().id(1L).name("Latam").code("LA").build()
        );

        AirlineResponse response = service.update(1L, request);

        assertThat(response.name()).isEqualTo("Latam");
        assertThat(response.code()).isEqualTo("LA");
        verify(repository).save(any(Airline.class));
    }

    @Test
    void update_ShouldThrow_WhenNotExists() {
        AirlineUpdateRequest request = new AirlineUpdateRequest("Latam", "LA");
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airline 99 not found");
    }

    @Test
    void delete_ShouldCallRepository() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}
