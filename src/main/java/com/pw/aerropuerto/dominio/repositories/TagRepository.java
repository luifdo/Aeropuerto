package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
@Query("SELECT t FROM Tag t where t.name = :name ")
Optional<Tag> findByName(@Param("name") String name);
@Query("SELECT t FROM Tag t where t.name = :name ")
Optional<Tag> findByName(@Param("name") List<String> name);
}
