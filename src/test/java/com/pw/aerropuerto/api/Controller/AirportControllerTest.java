package com.pw.aerropuerto.api.Controller;

import com.pw.aerropuerto.api.dto.AirportDtos.*;
import com.pw.aerropuerto.service.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AirportController.class)

class AirportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AirportService airportService;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

    var req = new AirportCreateRequest("CVA","Dorado","bogota");
    var resp = new AirportResponse(5L,"CVA","Dorado","bogota",java.util.Set.of(),java.util.Set.of());

    when(airportService.create(any())).thenReturn(resp);


     mockMvc.perform(post("/api/airports")
             .contentType(MediaType.APPLICATION_JSON)
             .content(objectMapper.writeValueAsString(req)))
             .andExpect(status().isCreated())
             .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/airports/5")))
             .andExpect(jsonPath("$.id").value(5));
    }
    @Test
    void ListByFlight_shouldReturn200() throws Exception {
        var page = new PageImpl<>(java.util.List.of(
                new AirportResponse(1L,"ABC","Coso","Santa marta", java.util.Set.of(),java.util.Set.of())
        ));



    }


}