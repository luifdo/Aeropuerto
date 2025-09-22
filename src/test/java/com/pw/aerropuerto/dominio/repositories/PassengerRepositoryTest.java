package com.pw.aerropuerto.dominio.repositories;

import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.entities.PassengerProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PassengerRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    PassengerRepository passengerRepository;

    @Test
    @DisplayName("Encuentra pasajero ignorando mayusculas/minusculas en el email")
    void findByEmailIgnoreCaseQuery() {

        var passenger = passengerRepository.save( Passenger.builder().email("USER@GMAIL.COM").build());

        Optional <Passenger> result = passengerRepository.findByEmailIgnoreCaseQuery("USER@GMAIL.COM");

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isNotNull();
        assertThat(result.get().getEmail()).isEqualTo("USER@GMAIL.COM");
    }

    @Test
    @DisplayName("Encuentra pasajero con su perfil")
    void findByEmailIgnoreCaseWithProfile() {
        var profile = PassengerProfile.builder()
                .phone("123456789")
                .countryCode("87").build();
        var passenger = passengerRepository.save( Passenger.builder()
                .email("coso@gmail.com")
                .profile(profile)
                .build());


        Optional<Passenger> result = passengerRepository.findByEmailIgnoreCaseWithProfile("coso@gmail.com");
        assertThat(result).isPresent();
        assertThat(result.get().getProfile()).isNotNull();
        assertThat(result.get().getProfile().getPhone()).isEqualTo("123456789");
        assertThat(result.get().getProfile().getCountryCode()).isEqualTo("87");
    }
}