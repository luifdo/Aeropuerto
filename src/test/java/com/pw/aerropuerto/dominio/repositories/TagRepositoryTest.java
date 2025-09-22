package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TagRepositoryTest  extends AbstractRepositoryIT{

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("Encontrar nombre ")
    void findByName() {
        var tag = tagRepository.save(Tag.builder().name("LOUIS").build());

        Optional<Tag> result = tagRepository.findByName("LOUIS");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotNull();
        assertThat(result.get().getName()).isEqualTo("LOUIS");

    }

    @Test
    @DisplayName("Encontrar nombre lista")
    void testFindByName() {
         tagRepository.saveAll(List.of(
                Tag.builder().name("CARLOS").build(),
                Tag.builder().name("Vegeta").build(),
                Tag.builder().name("Kadir").build()));

        List<Tag> result = tagRepository.findByNameIn((List.of("LOUIS","Vegeta","Kadir" )));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        


    }
}