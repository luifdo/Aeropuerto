package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.AirlineDtos.*;
import com.pw.aerropuerto.dominio.entities.Airline;
import com.pw.aerropuerto.dominio.repositories.AirlineRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplTest {

    @Mock
    AirlineRepository repository;
    @InjectMocks AirlineServiceImpl service;

    @Test
    void create() {
        var req = new AirlineCreateRequest("avianca","AV");
        when (repository.save(any())).thenAnswer(invocationOnMock -> {
            Airline entity = invocationOnMock.getArgument(0);
            entity.setId(10L);
            return entity;
        });
        var res = service.create(req);

        assertThat(res.id()).isEqualTo(10L);
        assertThat(res.name()).isEqualTo("avianca");
        assertThat(res.code()).isEqualTo("AV");
        verify(repository).save(any());
    }

    @Test
    void get() {
        var entity = Airline.builder().id(5L).name("LATAM").code("LA").build();
        when(repository.findById(5L)).thenReturn(Optional.of(entity));

        var res = service.get(5L);

        assertThat(res.id()).isEqualTo(5L);
        assertThat(res.name()).isEqualTo("LATAM");
        assertThat(res.code()).isEqualTo("LA");
    }

    @Test
    void getByCode() {
        when(repository.findByCode("XX")).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> service.getByCode("XX"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Airline XX not found");
    }

    @Test
    void lis() {
        var entity = Airline.builder().id(1L).name("VivaAir").code("VV").build();
        var page = new PageImpl<>(List.of(entity));
        var pageable = PageRequest.of(0, 2);

        when(repository.findAll(pageable)).thenReturn(page);

        var res = service.lis(pageable);

        assertThat(res.getTotalElements()).isEqualTo(1);
        assertThat(res.getContent().get(0).code()).isEqualTo("VV");
    }

    @Test
    void update() {
        var entity = Airline.builder().id(7L).name("OldName").code("ON").build();
        when(repository.findById(7L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var req = new AirlineUpdateRequest("NewName", "NN");
        var res = service.update(7L, req);

        assertThat(res.name()).isEqualTo("NewName");
        assertThat(res.code()).isEqualTo("NN");
        verify(repository).save(entity);
    }

    @Test
    void delete() {
        service.delete(12L);

        verify(repository).deleteById(12L);

    }
}