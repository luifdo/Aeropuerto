package com.pw.aerropuerto.api;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.aerropuerto.api.Controller.TagController;
import com.pw.aerropuerto.api.dto.TagDtos.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper om;

    @MockitoBean
    com.pw.aerropuerto.service.TagService tagService;

    @Test
    void createTag_shouldReturn201AndLocation() throws Exception {
        var request = new TagCreateRequest("VIP");
        var response = new TagResponse(1L, "VIP", java.util.Set.of());

        when(tagService.create(any())).thenReturn(response);

        mvc.perform(post("/api/Tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/Tags/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("VIP"))
                .andExpect(jsonPath("$.description").value("Etiqueta de cliente preferencial"));
    }

    @Test
    void getTag_shouldReturn200AndTag() throws Exception {
        var response = new TagResponse(2L, "BUSINESS", java.util.Set.of());

        when(tagService.get(2L)).thenReturn(response);

        mvc.perform(get("/api/Tags/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("BUSINESS"))
                .andExpect(jsonPath("$.description").value("Etiqueta para viajeros frecuentes"));
    }

    @Test
    void deleteTag_shouldReturn204() throws Exception {
        mvc.perform(delete("/api/Tags/3"))
                .andExpect(status().isNoContent());

        verify(tagService).delete(3L);
    }
}
