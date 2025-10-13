package com.pw.aerropuerto.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.SeatInventoryController;
import com.pw.aerropuerto.api.dto.FlightDtos;
import com.pw.aerropuerto.api.dto.SeatInventoryDtos.*;
import com.pw.aerropuerto.dominio.entities.*;
import com.pw.aerropuerto.service.SeatInventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeatInventoryController.class)
class SeatInventoryControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
      SeatInventoryService seatInventoryService;

    @Test
    void createSeatInventory_shouldReturn201AndLocation() throws Exception {
        var cabin = Cabin.valueOf("ECONOMY");
        var flight = new Flight(15L, "AVLLL",
                OffsetDateTime.of(2025, 12, 24, 8, 30, 0, 0,
                ZoneOffset.ofHours(1)), OffsetDateTime.of(2025, 12, 24, 9, 30, 0, 0,ZoneOffset.ofHours(1))
                ,new Airline(), new Airport(), new Airport(), java.util.Set.of());
        var req = new SeatInventoryRequest(cabin, 4, 15L);
        var resp = new SeatInventoryResponse(1L, cabin, 10, 4, 15L);

        when(seatInventoryService.create(any())).thenReturn(resp);

        mvc.perform(post("/api/SeatInventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req))).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/SeatInventory/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.flightId").value(15))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(10))
                .andExpect(jsonPath("$.availableSeats").value(4));

    }

    @Test
    void getSeatInventory_shouldReturn200() throws Exception {
        var cabin = Cabin.valueOf("ECONOMY");
        var flight = new Flight();
        var resp = new SeatInventoryResponse(2L, cabin, 60, 50, 30L);

        when(seatInventoryService.get(2L)).thenReturn(resp);

        mvc.perform(get("/api/SeatInventory/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.flightId").value(30))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(60))
                .andExpect(jsonPath("$.availableSeats").value(50));
    }

    @Test
    void listSeatInventories_shouldReturn200WithPage() throws Exception {
        var cabin = Cabin.valueOf("ECONOMY");
        var flight = new Flight();
        var list = List.of(new SeatInventoryResponse(3L, cabin, 124, 40, 35L));
        var page = new PageImpl<>(list, PageRequest.of(0, 10, Sort.by("id").ascending()), 1);

        when(seatInventoryService.list(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/SeatInventory?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.content[0].totalSeats").value(124))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void updateSeatInventory_shouldReturn200() throws Exception {
        var cabin = Cabin.valueOf("ECONOMY");
        var flight = new Flight();
        var req = new SeatInventoryRequest(cabin, 100, 140L);
        var resp = new SeatInventoryResponse(4L, cabin, 120, 100, 140L);

        when(seatInventoryService.update(eq(4L), any())).thenReturn(resp);

        mvc.perform(put("/api/SeatInventory/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.cabin").value("ECONOMY"))
                .andExpect(jsonPath("$.totalSeats").value(120))
                .andExpect(jsonPath("$.availableSeats").value(100));
    }

    @Test
    void deleteSeatInventory_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/SeatInventory/5"))
                .andExpect(status().isNoContent());

        verify(seatInventoryService).delete(5L);
    }}
