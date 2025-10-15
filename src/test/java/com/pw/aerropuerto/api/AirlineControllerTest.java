package com.pw.aerropuerto.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.AirlineController;
import com.pw.aerropuerto.api.dto.AirlineDtos.*;
import com.pw.aerropuerto.service.AirlineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirlineController.class)
class AirlineControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean
    AirlineService airlineService;

    @Test
    void addAirline_shouldReturn201AndLocation() throws Exception {
        var req = new AirlineCreateRequest("LATAM", "LA");
        var resp = new AirlineResponse(1L, "LATAM", "LA", java.util.Set.of());

        when(airlineService.create(any())).thenReturn(resp);

        mvc.perform(post("/api/airlines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/airlines/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("LATAM"));
    }

    @Test
    void getAirline_shouldReturn200() throws Exception {
        var resp = new AirlineResponse(2L, "Avianca", "AV", java.util.Set.of());
        when(airlineService.get(eq(2L))).thenReturn(resp);

        mvc.perform(get("/api/airlines/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("AV"))
                .andExpect(jsonPath("$.name").value("Avianca"));
    }

    @Test
    void list_shouldReturn200WithPagination() throws Exception {
        var page = new PageImpl<>(List.of(
                new AirlineResponse(3L, "Copa Airlines", "CM", java.util.Set.of())
        ));
        when(airlineService.lis(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/airlines?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].code").value("CM"));
    }

    @Test
    void getByCode_shouldReturn200() throws Exception {
        var resp = new AirlineResponse(4L, "Delta Airlines", "DL", java.util.Set.of());
        when(airlineService.getByCode(eq("DL"))).thenReturn(resp);

        mvc.perform(get("/api/airlines/by-code?code=DL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Delta Airlines"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new AirlineUpdateRequest("Iberia", "IB");
        var resp = new AirlineResponse(5L, "Iberia", "IB", java.util.Set.of());

        when(airlineService.update(eq(5L), any())).thenReturn(resp);

        mvc.perform(patch("/api/airlines/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Iberia"))
                .andExpect(jsonPath("$.code").value("IB"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/airlines/7"))
                .andExpect(status().isNoContent());
        verify(airlineService).delete(7L);
    }
}
