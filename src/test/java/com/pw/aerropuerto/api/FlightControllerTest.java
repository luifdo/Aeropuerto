package com.pw.aerropuerto.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.FlightController;
import com.pw.aerropuerto.api.dto.FlightDtos.*;
import com.pw.aerropuerto.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    FlightService flightService;

    @Test
    void createFlight_shouldReturn201AndLocation() throws Exception {
        var departure = OffsetDateTime.now(ZoneOffset.UTC).plusHours(3);
        var arrival = OffsetDateTime.now(ZoneOffset.UTC).plusHours(7);
        var req = new FlightCreateRequest("AV101", departure, arrival);
        var resp = new FlightResponse(1L, "AV101", departure, arrival, 67L, "Santa Marta","Bogota",java.util.Set.of());

        when(flightService.create(any())).thenReturn(resp);

        mvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/flights/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("AV101"));
    }

    @Test
    void getFlight_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1);
        var arrival = OffsetDateTime.now(ZoneOffset.UTC).plusHours(4);
        var resp = new FlightResponse(2L, "LA202",departure, arrival, 67L, "Santa Marta","Bogota",java.util.Set.of());
        when(flightService.get(2L)).thenReturn(resp);

        mvc.perform(get("/api/flights/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.origin").value("LIM"));
    }

    @Test
    void listFlights_shouldReturn200WithPage() throws Exception {
        var departure = OffsetDateTime.now(ZoneOffset.UTC).plusHours(6);
        var arrival = OffsetDateTime.now(ZoneOffset.UTC).plusHours(8);
        var list = List.of(new FlightResponse(3L, "IB303", departure, arrival, 67L, "Santa Marta","Bogota",java.util.Set.of()));
        var page = new PageImpl<>(list, PageRequest.of(0, 10, Sort.by("id").ascending()), 1);

        when(flightService.list(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/flights?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value("IB303"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void updateFlight_shouldReturn200() throws Exception {
        var departure = OffsetDateTime.now(ZoneOffset.UTC).plusHours(9);
        var arrival = OffsetDateTime.now(ZoneOffset.UTC).plusHours(15);
        var req = new FlightUpdateRequest("AV102", departure, arrival);
        var resp = new FlightResponse(4L, "AV102", departure, arrival, 67L, "Santa Marta","Bogota",java.util.Set.of());;

        when(flightService.update(eq(4L), any())).thenReturn(resp);

        mvc.perform(patch("/api/flights/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.destination").value("NYC"));
    }

    @Test
    void deleteFlight_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/flights/5"))
                .andExpect(status().isNoContent());
        verify(flightService).delete(5L);
    }}
