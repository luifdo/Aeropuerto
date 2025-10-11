package com.pw.aerropuerto.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.BookingController;
import com.pw.aerropuerto.api.dto.BookingDtos.*;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.entities.PassengerProfile;
import com.pw.aerropuerto.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean BookingService service;

    @Test
    void createBooking_shouldReturn201AndLocation() throws Exception {
        var req = new BookingCreateRequest(OffsetDateTime.of(2025, 10, 10, 15, 30, 0, 0, ZoneOffset.of("-05:00")), 4L, java.util.List.of());
        var resp = new BookingResponse(10L, OffsetDateTime.of(2025, 10, 10, 15, 30, 0, 0, ZoneOffset.of("-05:00")) ,java.util.List.of(), 200L);

        when(service.create(any(), any(), any())).thenReturn(resp);

        mvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/bookings/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.code").value("RES-001"));
    }

    @Test
    void getBookings_shouldReturn200WithPagination() throws Exception {
        var list = List.of(new BookingResponse(10L, OffsetDateTime.of(2025, 10, 10, 15, 30, 0, 0, ZoneOffset.of("-05:00")) ,java.util.List.of(), 200L));
        var page = new PageImpl<>(list, PageRequest.of(0, 10, Sort.by("id").ascending()), 1);

        when(service.list(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/bookings?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value("RES-002"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getByPassenger_shouldReturn200() throws Exception {
        var resp = new BookingResponse(10L, OffsetDateTime.of(2025, 10, 10, 15, 30, 0, 0, ZoneOffset.of("-05:00")) ,java.util.List.of(), 200L);
        when(service.get(any())).thenReturn(resp);

        mvc.perform(get("/api/bookings/by-passanger?passenger=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos Pérez"))
                .andExpect(jsonPath("$.cabinType").value("PREMIUM"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new BookingUpdateRequest(OffsetDateTime.of(2025, 10, 10, 15, 30, 0, 0, ZoneOffset.of("-05:00")) ,
                new Passenger(3L,"Luis fernando","lf@.com", new PassengerProfile(),java.util.Set.of(), new PassengerProfile()), java.util.List.of());
        var resp = new BookingResponse(10L, OffsetDateTime.of(2025, 10, 10, 15, 30, 0, 0, ZoneOffset.of("-05:00")) ,java.util.List.of(), 200L);

        when(service.update(eq(13L), any())).thenReturn(resp);

        mvc.perform(patch("/api/bookings/13")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("UPDATED-001"))
                .andExpect(jsonPath("$.cabinType").value("BUSINESS"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/bookings/14"))
                .andExpect(status().isNoContent());
        verify(service).delete(14L);
    }}
