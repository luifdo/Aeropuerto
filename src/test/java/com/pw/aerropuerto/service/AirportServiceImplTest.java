package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.AirportDtos.*;
import com.pw.aerropuerto.dominio.entities.Airport;
import com.pw.aerropuerto.dominio.repositories.AirportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class AirportServiceImplTest {

    @Mock
    AirportRepository repository;
    @InjectMocks
    AirportServiceImpl airportService;
    @Test
    void create() {
        var req = new AirportCreateRequest("El Dorado", "BOG");
        when(repository.save(any())).thenAnswer(inv -> {
            Airport entity = inv.getArgument(0);
            entity.setId(1L);
            return entity;
        });
        var res =  airportService.create(req);

        assertThat(res.id()).isEqualTo(1L);
        assertThat(res.destinations()).isEqualTo("El Dorado");
        assertThat(res.Code()).isEqualTo("BOG");
        verify(repository).save(any());
    }

    @Test
    void get() {

    }

    @Test
    void testGet() {
    }

    @Test
    void list() {
    }

    @Test
    void testList() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}