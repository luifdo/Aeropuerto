package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airline;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AirlineRepositoryTest extends AbstractRepositoryIT {

   @Autowired
   private AirlineRepository airlineRepository;

   @Test
    @DisplayName("Recupere una aerolinea por su codigo")
    void findByCode() {
       var airline = airlineRepository.save(Airline.builder().code("AV").name("Avianca").build());

       Optional<Airline> result = airlineRepository.findByCode("AV");

       assertTrue(result.isPresent());
       assertThat(result.get().getId()).isNotNull();
       assertThat(result.get().getName()).isEqualTo("Avianca");
       assertThat(result.get().getCode()).isEqualTo("AV");
   }

}