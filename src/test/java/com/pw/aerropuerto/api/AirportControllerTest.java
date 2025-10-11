package com.pw.aerropuerto.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.AirportController;
import com.pw.aerropuerto.api.dto.AirportDtos.*;
import com.pw.aerropuerto.service.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirportController.class)
class AirportControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @MockitoBean
     private AirportService airportService;

    @Test
    void createAirport_shouldReturn201AndLocation() throws Exception {
        var req = new AirportCreateRequest("BOG", "El Dorado", "Bogotá");
        var resp = new AirportResponse(1L, "BOG", "El Dorado", "Bogotá", java.util.Set.of(), java.util.Set.of());

        when(airportService.create(any())).thenReturn(resp);

        mvc.perform(post("/api/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/airports/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.Name").value("El Dorado"));
    }

    @Test
    void getAirport_shouldReturn200() throws Exception {
        var resp = new AirportResponse(2L, "MDE", "José María Córdova", "Medellín", java.util.Set.of(), java.util.Set.of());
        when(airportService.get(eq(2L))).thenReturn(resp);

        mvc.perform(get("/api/airports/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Code").value("MDE"))
                .andExpect(jsonPath("$.Name").value("José María Córdova"));
    }

    @Test
    void getAllAirports_shouldReturn200WithPagination() throws Exception {
        var page = new PageImpl<>(List.of(
                new AirportResponse(3L, "CTG", "Rafael Núñez", "Cartagena", java.util.Set.of(), java.util.Set.of())
        ));
        when(airportService.list(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/airports?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].Code").value("CTG"));
    }

    @Test
    void getByCode_shouldReturn200() throws Exception {
        var resp = new AirportResponse(4L, "PEI", "Matecaña", "Pereira", java.util.Set.of(), java.util.Set.of());
        when(airportService.get(eq("PEI"))).thenReturn(resp);

        mvc.perform(get("/api/airports/by-code?code=PEI"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Name").value("Matecaña"))
                .andExpect(jsonPath("$.City").value("Pereira"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new AirportUpdateRequest("BAQ", "Ernesto Cortissoz", "Barranquilla");
        var resp = new AirportResponse(5L, "BAQ", "Ernesto Cortissoz", "Barranquilla", java.util.Set.of(), java.util.Set.of());

        when(airportService.update(eq(5L), any())).thenReturn(resp);

        mvc.perform(patch("/api/airports/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Name").value("Ernesto Cortissoz"))
                .andExpect(jsonPath("$.Code").value("BAQ"));
    }

    @Test
    void deleteAirport_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/airports/6"))
                .andExpect(status().isNoContent());
        verify(airportService).delete(6L);
    }
}