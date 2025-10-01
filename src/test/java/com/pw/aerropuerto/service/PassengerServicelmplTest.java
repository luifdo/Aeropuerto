package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.PassangerDtos.PassengerCreateRequest;
import com.pw.aerropuerto.api.dto.PassangerDtos.PassengerUpdateRequest;
import com.pw.aerropuerto.api.dto.PassangerDtos.PassengerResponse;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.repositories.PassengerRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import com.pw.aerropuerto.service.mapper.PassengerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @Mock
    private PassengerRepository repository;

    @InjectMocks
    private PassengerServiceImpl service;

    @Test
    void create_debe_guardar_y_devolver_response() {
        PassengerCreateRequest req = mock(PassengerCreateRequest.class);

        Passenger saved = Passenger.builder()
                .Id(101L)
                .email("test@example.com")
                .build();

        // stub repository.save
        when(repository.save(any(Passenger.class))).thenReturn(saved);

        // mock static mapper toResponse
        PassengerResponse respMock = mock(PassengerResponse.class);
        when(respMock.Id()).thenReturn(101L);
        when(respMock.email()).thenReturn("test@example.com");

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            // When toResponse is called with any Passenger, return our mock response
            mocked.when(() -> PassengerMapper.toResponse(any(Passenger.class))).thenReturn(respMock);

            var resp = service.create(req);

            assertThat(resp).isNotNull();
            assertThat(resp.Id()).isEqualTo(101L);
            assertThat(resp.email()).isEqualTo("test@example.com");

            verify(repository, times(1)).save(any(Passenger.class));
            mocked.verify(() -> PassengerMapper.toResponse(any(Passenger.class)), times(1));
        }
    }

    @Test
    void get_debe_devolver_response_si_existe() {
        Passenger p = Passenger.builder().Id(5L).email("a@b.com").build();
        when(repository.findById(5L)).thenReturn(Optional.of(p));

        PassengerResponse respMock = mock(PassengerResponse.class);
        when(respMock.Id()).thenReturn(5L);
        when(respMock.email()).thenReturn("a@b.com");

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            mocked.when(() -> PassengerMapper.toResponse(any(Passenger.class))).thenReturn(respMock);

            var resp = service.get(5L);

            assertThat(resp).isNotNull();
            assertThat(resp.Id()).isEqualTo(5L);
            assertThat(resp.email()).isEqualTo("a@b.com");

            verify(repository).findById(5L);
        }
    }

    @Test
    void get_debe_lanzar_NotFoundException_si_no_existe() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Passenger 999 not found");
    }

    @Test
    void getByEmail_debe_devolver_response_si_existe() {
        String email = "mi@correo.com";
        Passenger p = Passenger.builder().Id(7L).email(email).build();
        when(repository.findByEmailIgnoreCaseQuery(email)).thenReturn(Optional.of(p));

        PassengerResponse respMock = mock(PassengerResponse.class);
        when(respMock.Id()).thenReturn(7L);
        when(respMock.email()).thenReturn(email);

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            mocked.when(() -> PassengerMapper.toResponse(any(Passenger.class))).thenReturn(respMock);

            var resp = service.getByEmail(email);

            assertThat(resp).isNotNull();
            assertThat(resp.Id()).isEqualTo(7L);
            assertThat(resp.email()).isEqualTo(email);

            verify(repository).findByEmailIgnoreCaseQuery(email);
        }
    }

    @Test
    void getByEmail_debe_lanzar_NotFoundException_si_no_existe() {
        String email = "noexiste@x.com";
        when(repository.findByEmailIgnoreCaseQuery(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByEmail(email))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Passenger " + email + " not found");
    }

    @Test
    void lis_debe_devolver_pagina_de_responses() {
        Passenger a = Passenger.builder().Id(1L).email("a@a.com").build();
        Passenger b = Passenger.builder().Id(2L).email("b@b.com").build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Passenger> page = new PageImpl<>(List.of(a, b), pageable, 2);

        when(repository.findAll(pageable)).thenReturn(page);

        PassengerResponse respA = mock(PassengerResponse.class);
        PassengerResponse respB = mock(PassengerResponse.class);

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            // map each Passenger to a response mock
            mocked.when(() -> PassengerMapper.toResponse(a)).thenReturn(respA);
            mocked.when(() -> PassengerMapper.toResponse(b)).thenReturn(respB);

            Page<PassengerResponse> respPage = service.lis(pageable);

            assertThat(respPage).isNotNull();
            assertThat(respPage.getTotalElements()).isEqualTo(2);
            assertThat(respPage.getContent()).hasSize(2);
            // verify repository call
            verify(repository).findAll(pageable);
        }
    }

    @Test
    void update_debe_aplicar_path_y_devolver_response_si_existe() {
        Long id = 50L;
        Passenger existing = Passenger.builder().Id(id).email("old@x.com").build();
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        PassengerUpdateRequest req = mock(PassengerUpdateRequest.class);

        PassengerResponse respMock = mock(PassengerResponse.class);
        when(respMock.Id()).thenReturn(id);
        when(respMock.email()).thenReturn("new@x.com");

        try (MockedStatic<PassengerMapper> mocked = mockStatic(PassengerMapper.class)) {
            // stub path to do nothing (it would mutate 'existing' in real code)
            mocked.when(() -> PassengerMapper.path(any(Passenger.class), any(PassengerUpdateRequest.class)))
                    .thenAnswer(invocation -> {
                        Passenger p = invocation.getArgument(0);
                        // simulate path mutating passenger (optional)
                        p.setEmail("new@x.com");
                        return null;
                    });

            mocked.when(() -> PassengerMapper.toResponse(any(Passenger.class))).thenReturn(respMock);

            var resp = service.update(id, req);

            assertThat(resp).isNotNull();
            assertThat(resp.Id()).isEqualTo(id);
            assertThat(resp.email()).isEqualTo("new@x.com");

            verify(repository).findById(id);
            mocked.verify(() -> PassengerMapper.path(existing, req), times(1));
            mocked.verify(() -> PassengerMapper.toResponse(existing), times(1));
        }
    }

    @Test
    void update_debe_lanzar_NotFoundException_si_no_existe() {
        Long id = 1234L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        PassengerUpdateRequest req = mock(PassengerUpdateRequest.class);

        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Passenger 1234 not found");
    }

    @Test
    void delete_debe_eliminar_por_id() {
        Long id = 9L;
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}
