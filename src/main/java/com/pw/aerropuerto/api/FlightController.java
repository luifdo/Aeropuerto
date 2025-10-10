package com.pw.aerropuerto.api;

import com.pw.aerropuerto.api.dto.FlightDtos.*;
import com.pw.aerropuerto.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Validated
public class FlightController {
    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponse> createFlight(@Valid@RequestBody FlightCreateRequest request, UriComponentsBuilder uriComponentsBuilder) {
        var body = flightService.create(request);
        var location = uriComponentsBuilder.path("/api/flights/{id}").buildAndExpand(body).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FlightResponse> getFlight(@PathVariable long id) {
        return ResponseEntity.ok(flightService.get(id));
    }
    @GetMapping
    public ResponseEntity<Page<FlightResponse>> getAllAirlines(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size){
        var result = flightService.list(PageRequest.of(page,size, Sort.by("id").ascending()));
        return ResponseEntity.ok().body(result);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<FlightResponse> updateFlight(@PathVariable long id, @Valid @RequestBody FlightUpdateRequest request){
        return ResponseEntity.ok(flightService.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<FlightResponse> deleteFlight(@PathVariable long id){
        flightService.delete(id);
        return ResponseEntity.noContent().build();
    }
 }
