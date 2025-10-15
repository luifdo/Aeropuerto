package com.pw.aerropuerto.api.Controller;

import com.pw.aerropuerto.api.dto.AirlineDtos.*;
import com.pw.aerropuerto.service.AirlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
@Value
public class AirlineController {
    private final AirlineService airlineService;

    @PostMapping
    public ResponseEntity<AirlineResponse> addAirline(@Valid @RequestBody AirlineCreateRequest request,
                                              UriComponentsBuilder uriComponentsBuilder) {
        var body = airlineService.create(request);
        var location = uriComponentsBuilder.path("/api/airlines/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirlineResponse> getAirline(@PathVariable Long id) {
        return ResponseEntity.ok(airlineService.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<AirlineResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        var result = airlineService.lis(PageRequest.of(page, size, Sort.by("id").ascending()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-code")
    public ResponseEntity<AirlineResponse> getByCode(@RequestParam String code) {
        return ResponseEntity.ok(airlineService.getByCode(code));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AirlineResponse> update(@PathVariable Long id,
                                                  @Valid@RequestBody AirlineUpdateRequest request){
        return ResponseEntity.ok(airlineService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        airlineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

