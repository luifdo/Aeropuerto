package com.pw.aerropuerto.api;

import com.pw.aerropuerto.api.dto.PassangerDtos.*;
import com.pw.aerropuerto.service.PassengerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PassangerControllerTest.class)
class PassangerControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;
    @MockitoBean
        PassengerService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {

        var req = new PassengerCreateRequest("Sebas","nene@.com", new PassengerProfileDto("+57", "Col"));
        var resp = new PassengerResponse(10L, "Sebas", "nene@.com", new PassengerProfileDto("+57", "Col"));

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/passengers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/passengers/10")))
                .andExpect(jsonPath("$.id").value(10));

    }

}