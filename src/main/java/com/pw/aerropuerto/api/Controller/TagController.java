package com.pw.aerropuerto.api.Controller;

import com.pw.aerropuerto.api.dto.TagDtos.*;
import com.pw.aerropuerto.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/Tags")
@RequiredArgsConstructor
@Validated
public class TagController {
    private TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid@RequestBody TagCreateRequest request, UriComponentsBuilder uriComponentsBuilder) {
        var body = tagService.create(request);
        var location = uriComponentsBuilder.path("/api/Tags/{id}").buildAndExpand(body).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> getTag(@PathVariable long id) {
        return ResponseEntity.ok(tagService.get(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<TagResponse> deleteTag(@PathVariable long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
