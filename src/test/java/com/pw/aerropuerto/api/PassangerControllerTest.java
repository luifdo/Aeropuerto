package com.pw.aerropuerto.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.PassangerController;
import com.pw.aerropuerto.api.dto.PassangerDtos.*;
import com.pw.aerropuerto.service.PassengerService;
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

@WebMvcTest(PassangerController.class)
class PassengerControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockitoBean PassengerService passengerService;

    @Test
    void createPassenger_shouldReturn201AndLocation() throws Exception {
        var req = new PassengerCreateRequest("Ana", "ana@correo.com", new PassengerProfileDto("+57 319 7604110","COL"));
        var resp = new PassengerResponse(1L, "Ana", "ana@correo.com", new PassengerProfileDto("+57 319 7604110","COL"));

        when(passengerService.create(any())).thenReturn(resp);

        mvc.perform(post("/api/passengers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/passengers/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ana"));
    }

    @Test
    void getPassenger_shouldReturn200() throws Exception {
        var resp = new PassengerResponse(2L, "Luis", "luis@mail.com", new PassengerProfileDto("+57 319 7604110","COL"));
        when(passengerService.get(eq(2L))).thenReturn(resp);

        mvc.perform(get("/api/passengers/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luis"))
                .andExpect(jsonPath("$.email").value("luis@mail.com"));
    }

    @Test
    void getByEmail_shouldReturn200() throws Exception {
        var resp = new PassengerResponse(3L, "Carlos", "carlos@mail.com", new PassengerProfileDto("+57 319 7604110","COL"));
        when(passengerService.get(anyLong())).thenReturn(resp);

        mvc.perform(get("/api/passengers/by-Email?Email=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@mail.com"));
    }

    @Test
    void getAllPassengers_shouldReturn200WithPagination() throws Exception {
        var page = new PageImpl<>(List.of(
                new PassengerResponse(4L, "Maria", "maria@mail.com", new PassengerProfileDto("+57 319 7604110","COL"))
        ));
        when(passengerService.lis(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/passengers?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Maria"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new PassengerUpdateRequest("Pedro", "pedro@mail.com",new PassengerProfileDto("+57 319 7604110","COL"));
        var resp = new PassengerResponse(5L, "Pedro", "pedro@mail.com",new PassengerProfileDto("+57 319 7604110","COL"));

        when(passengerService.update(eq(5L), any())).thenReturn(resp);

        mvc.perform(get("/api/passengers/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@mail.com"));
    }

    @Test
    void deletePassenger_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/passengers/6"))
                .andExpect(status().isNoContent());
        verify(passengerService).delete(6L);
    }
}