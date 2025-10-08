package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.BookItemDtos.BookItemCreateRequest;
import com.pw.aerropuerto.api.dto.BookItemDtos.BookItemUpdateRequest;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Cabin;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.dominio.repositories.BookItemRepository;
import com.pw.aerropuerto.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingItemServiceImplTest {

    @Mock
    private BookItemRepository repository;

    @InjectMocks
    private BookingItemServicelmpl service;
    @BeforeEach
    void setUp() {
    }

    @Test
    void crear_debe_guardar_y_devolver_response() {

        // Mock del DTO (si son record, mockear sus métodos)
        BookItemCreateRequest req = mock(BookItemCreateRequest.class);
        when(req.cabinType()).thenReturn("ECONOMY");
        when(req.price()).thenReturn(BigDecimal.valueOf(150.0));
        when(req.bookingId()).thenReturn(1L);
        when(req.flightId()).thenReturn(10L);




        // Entidad que el repo "devuelve" tras save
        BookItem saved = BookItem.builder()
                .id(100L)
                .cabin(Cabin.ECONOMY)
                .price(BigDecimal.valueOf(150.0))
                .segmentOrder(1)
                .flight(Flight.builder().build())
                // no seteamos booking (depende de tu entidad), solo lo mínimo
                .build();

        when(repository.save(any(BookItem.class))).thenReturn(saved);

        var resp = service.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(100L);
        assertThat(resp.cabinType()).isEqualTo("ECONOMY");
        assertThat(resp.price()).isEqualByComparingTo(BigDecimal.valueOf(150.0));

        // Verificar que se llamó repository.save con una BookItem (mapper produce la entidad)
        verify(repository, times(1)).save(any(BookItem.class));
    }

    @Test
    void get_debe_devolver_response_si_existe() {
        BookItem item = BookItem.builder()
                .id(5L)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(300.0))
                .segmentOrder(2)
                .flight(Flight.builder().build())
                .build();

        when(repository.findById(5L)).thenReturn(Optional.of(item));

        var resp = service.get(5L);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(5L);
        assertThat(resp.cabinType()).isEqualTo("BUSINESS");
        assertThat(resp.price()).isEqualByComparingTo(BigDecimal.valueOf(300.0));
    }

    @Test
    void get_debe_lanzar_NotFoundException_si_no_existe() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("BookItem not found");
    }

    @Test
    void list_debe_devolver_pagina_de_responses() {
        BookItem a = BookItem.builder().id(1L).cabin(Cabin.ECONOMY).price(BigDecimal.valueOf(50.0)).segmentOrder(1).flight(Flight.builder().build()).build();
        BookItem b = BookItem.builder().id(2L).cabin(Cabin.PREMIUM).price(BigDecimal.valueOf(120.0)).segmentOrder(1).flight(Flight.builder().build()).build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<BookItem> page = new PageImpl<>(List.of(a, b), pageable, 2);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<?> respPage = service.list(pageable);

        assertThat(respPage).isNotNull();
        assertThat(respPage.getTotalElements()).isEqualTo(2);
        assertThat(respPage.getContent()).hasSize(2);
        // check mapped fields in first element (assumimos que mapper devuelve fields como string/number)
        var first = respPage.getContent().get(0);
        assertThat(first).isNotNull();
    }

    @Test
    void update_debe_modificar_campos_y_guardar_si_existe() {
        Long id = 20L;
        BookItem existing = BookItem.builder()
                .id(id)
                .cabin(Cabin.ECONOMY)
                .price(BigDecimal.valueOf(80.0))
                .segmentOrder(1)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existing));

        BookItemUpdateRequest req = mock(BookItemUpdateRequest.class);
        when(req.cabinType()).thenReturn("BUSINESS"); // nota: BookItemServiceImpl hace Cabin.valueOf(req.cabinType())
        when(req.price()).thenReturn(BigDecimal.valueOf(400.0));
        when(req.segmentOrder()).thenReturn(2);

        // After update, repo.save returns updated entity
        BookItem updated = BookItem.builder()
                .id(id)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(400.0))
                .segmentOrder(2)
                .build();

        // capture what's saved
        when(repository.save(any(BookItem.class))).thenReturn(updated);

        var resp = service.update(id, req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(id);
        assertThat(resp.cabinType()).isEqualTo("BUSINESS");
        assertThat(resp.price()).isEqualByComparingTo(BigDecimal.valueOf(400.0));

        // verify repository.findById and save called
        verify(repository).findById(id);
        ArgumentCaptor<BookItem> captor = ArgumentCaptor.forClass(BookItem.class);
        verify(repository).save(captor.capture());
        BookItem savedArg = captor.getValue();
        assertThat(savedArg.getCabin()).isEqualTo(Cabin.BUSINESS);
        assertThat(savedArg.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(400.0));
        assertThat(savedArg.getSegmentOrder()).isEqualTo(2);
    }

    @Test
    void update_debe_lanzar_NotFoundException_si_no_existe() {
        Long id = 300L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        BookItemUpdateRequest req = mock(BookItemUpdateRequest.class);
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("BookItem not found");
    }

    @Test
    void delete_debe_eliminar_por_id() {
        Long id = 7L;
        // no necesitamos stub para deleteById si solo verificamos invocación
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository, times(1)).deleteById(id);
    }
}