package com.pw.aerropuerto.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.BookItemsController;
import com.pw.aerropuerto.api.dto.BookItemDtos.*;
import com.pw.aerropuerto.service.BookingItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookItemsController.class)
class BookItemsControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    BookingItemService service;

    @Test
    void create_shouldReturn201AndLocation() throws Exception {
        var req = new BookItemCreateRequest("ECONOMY", BigDecimal.valueOf(12.00), 20L, 250L);
        var resp = new BookItemResponse(10L, "ECONOMY", 250L, BigDecimal.valueOf(12.00),4,9L);

        when(service.create(any())).thenReturn(resp);

        mvc.perform(post("/api/bookItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/bookItems/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.cabinType").value("ECONOMY"));
    }

    @Test
    void list_shouldReturn200WithPagination() throws Exception {
        var list = List.of(new BookItemResponse(10L, "ECONOMY", 250L, BigDecimal.valueOf(12.00),4,9L));
        var page = new PageImpl<>(list, PageRequest.of(0, 10, Sort.by("id").ascending()), 1);

        when(service.list(any(PageRequest.class))).thenReturn(page);

        mvc.perform(get("/api/bookItems?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cabinType").value("ECONOMY"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getByFlight_shouldReturn200() throws Exception {
        var resp = new BookItemResponse(10L, "ECONOMY", 250L, BigDecimal.valueOf(12.00),4,9L);
        when(service.get(anyLong())).thenReturn(resp);

        mvc.perform(get("/api/bookItems/by-flight?flight=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.cabinType").value("ECONOMY"));
    }

    @Test
    void update_shouldReturn200() throws Exception {
        var req = new BookItemUpdateRequest("BUSINESS", BigDecimal.valueOf(12.00),4);
        var resp = new BookItemResponse(10L, "ECONOMY", 250L, BigDecimal.valueOf(12.00),4,9L);

        when(service.update(eq(40L), any())).thenReturn(resp);

        mvc.perform(patch("/api/bookItems/40")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.cabinType").value("ECONOMY"))
                .andExpect(jsonPath("$.price").value(12.00));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/bookItems/50"))
                .andExpect(status().isNoContent());
        verify(service).delete(50L);
    }}
