package com.pw.aerropuerto.api;
import com.pw.aerropuerto.api.dto.AirportDtos.*;
import com.pw.aerropuerto.service.AirportService;
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
@RequestMapping("/api/airports")
@RequiredArgsConstructor
@Validated
public class AirportController {
    private AirportService airportService;

    @PostMapping
    public ResponseEntity<AirportResponse> createAirline(@Valid@RequestBody AirportCreateRequest request,
                                                      UriComponentsBuilder uriComponentsBuilder) {
        var body = airportService.create(request);
        var location = uriComponentsBuilder.path("/api/airlines/{id}").buildAndExpand(body).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> getAirline(@PathVariable long id) {
        return ResponseEntity.ok(airportService.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<AirportResponse>> getAllAirlines(@RequestParam(defaultValue = "0")int page,
                                                                @RequestParam(defaultValue = "10")int size) {
        var result = airportService.list(PageRequest.of(page,size, Sort.by("id").ascending()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-code")
    public ResponseEntity<AirportResponse> getbyCode(@RequestParam String code) {
        return ResponseEntity.ok(airportService.get(code));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AirportResponse> update(@PathVariable long id,
                                                  @Valid@RequestBody AirportUpdateRequest request) {
        return ResponseEntity.ok(airportService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AirportResponse> delete(@PathVariable long id) {
        airportService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
