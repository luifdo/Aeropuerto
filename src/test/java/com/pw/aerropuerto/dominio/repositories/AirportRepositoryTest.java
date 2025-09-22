package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Airport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AirportRepositoryTest extends  AbstractRepositoryIT {

    @Autowired
    private AirportRepository airportRepository;

    @Test
    @DisplayName("Recupere un aeropuerto por su codigo")
    void findByCode()
    {
        var airport = airportRepository.save(Airport.builder().Name("El dorado internacional airport").Code("BOG").build());

        Optional <Airport> result = airportRepository.findByCode("BOG");


        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotNull();
        assertThat(result.get().getCode()).isEqualTo("BOG");
        assertThat(result.get().getName()).isEqualTo("El dorado internacional airport");

    }

}