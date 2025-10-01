package com.pw.aerropuerto.service;

import com.pw.aerropuerto.api.dto.TagDtos.TagCreateRequest;
import com.pw.aerropuerto.api.dto.TagDtos.TagResponse;
import com.pw.aerropuerto.api.dto.TagDtos.TagUpdateRequest;
import com.pw.aerropuerto.dominio.entities.Tag;
import com.pw.aerropuerto.dominio.repositories.TagRepository;
import com.pw.aerropuerto.exception.NotFoundException;
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
class TagServiceImplTest {

    @Mock
    private TagRepository repository;

    @InjectMocks
    private TagServiceImpl service;

    @Test
    void create_debe_guardar_y_devolver_response() {
        TagCreateRequest req = mock(TagCreateRequest.class);
        Tag saved = Tag.builder().id(1L).name("VIP").build();

        when(repository.save(any(Tag.class))).thenReturn(saved);

        TagResponse resp = service.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(1L);
        assertThat(resp.name()).isEqualTo("VIP");

        verify(repository).save(any(Tag.class));
    }

    @Test
    void get_debe_devolver_response_si_existe() {
        Tag tag = Tag.builder().id(2L).name("DISCOUNT").build();
        when(repository.findById(2L)).thenReturn(Optional.of(tag));

        TagResponse resp = service.get(2L);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(2L);
        assertThat(resp.name()).isEqualTo("DISCOUNT");
    }

    @Test
    void get_debe_lanzar_NotFoundException_si_no_existe() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(10L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tag 10 not found");
    }

    @Test
    void list_debe_devolver_lista_de_responses() {
        Tag a = Tag.builder().id(1L).name("A").build();
        Tag b = Tag.builder().id(2L).name("B").build();
        when(repository.findAll()).thenReturn(List.of(a, b));

        List<TagResponse> respList = service.List();

        assertThat(respList).hasSize(2);
        assertThat(respList).extracting("name").containsExactly("A", "B");
    }

    @Test
    void update_debe_modificar_si_existe() {
        Long id = 5L;
        Tag existing = Tag.builder().id(id).name("OLD").build();
        when(repository.findById(id)).thenReturn(Optional.of(existing));

        TagUpdateRequest req = mock(TagUpdateRequest.class);
        // No necesitamos configurar métodos porque se aplican vía TagMapper.path()

        TagResponse resp = service.update(id, req);

        assertThat(resp).isNotNull();
        assertThat(resp.id()).isEqualTo(id);

        verify(repository).findById(id);
    }

    @Test
    void update_debe_lanzar_NotFoundException_si_no_existe() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        TagUpdateRequest req = mock(TagUpdateRequest.class);

        assertThatThrownBy(() -> service.update(99L, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tag 99 not found");
    }

    @Test
    void delete_debe_eliminar_por_id() {
        Long id = 7L;
        doNothing().when(repository).deleteById(id);

        service.delete(id);

        verify(repository).deleteById(id);
    }
}

