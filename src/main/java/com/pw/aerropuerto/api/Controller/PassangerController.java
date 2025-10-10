package com.pw.aerropuerto.api.Controller;

import com.pw.aerropuerto.api.dto.PassangerDtos.*;
import com.pw.aerropuerto.service.PassengerService;
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
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
@Validated
public class PassangerController {
    private PassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerResponse> createPassenger(@Valid@RequestBody PassengerCreateRequest request, UriComponentsBuilder uriComponentsBuilder) {
        var body = passengerService.create(request);
        var location = uriComponentsBuilder.path("/api/passengers/{id}").buildAndExpand(body).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> getPassenger(@PathVariable long id) {
        return ResponseEntity.ok(passengerService.get(id));
    }
    @GetMapping("/{by-Email}")
    public ResponseEntity<PassengerResponse> getByEmail(@RequestParam String Email) {
        return ResponseEntity.ok(passengerService.get(Long.valueOf(Email)));
    }

    @GetMapping
    public ResponseEntity<Page<PassengerResponse>> getAllPassengers(@RequestParam(defaultValue = "0")int page,
                                                                    @RequestParam(defaultValue = "10")int size){
    var result = passengerService.lis(PageRequest.of(page, size, Sort.by("name").ascending()));
    return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> update(@PathVariable long id,
                                                    @Valid@RequestBody PassengerUpdateRequest request) {
        return ResponseEntity.ok(passengerService.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponse> deletePassenger(@PathVariable long id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
