package com.pw.aerropuerto.api.Controller;

import com.pw.aerropuerto.api.dto.SeatInventoryDtos.*;
import com.pw.aerropuerto.service.SeatInventoryService;
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
@RequestMapping("/api/SeatInventories")
@RequiredArgsConstructor
@Validated
public class SeatInventoryController {
    private SeatInventoryService seatInventoryService;

    @PostMapping
    public ResponseEntity<SeatInventoryResponse> createSeatInventoryRespone(@Valid @RequestBody SeatInventoryRequest request, UriComponentsBuilder uriComponentsBuilder) {
        var body = seatInventoryService.create(request);
        var location = uriComponentsBuilder.path("/api/SeatInventories/{id}").buildAndExpand(body).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SeatInventoryResponse> getSeatInventoryResponse(@PathVariable long id){
        return ResponseEntity.ok(seatInventoryService.get(id));
    }
    @GetMapping
    public ResponseEntity<Page<SeatInventoryResponse>> getAllSeatInventory(@RequestParam(defaultValue = "0")int page,
                                                                                 @RequestParam(defaultValue = "0")int size){
    var result = seatInventoryService.list(PageRequest.of(page, size, Sort.by("id").ascending()));
    return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SeatInventoryResponse>update(@PathVariable long id,
                                                       @Valid @RequestBody SeatInventoryRequest req) {
        return ResponseEntity.ok(seatInventoryService.update(id, req));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<SeatInventoryResponse> deleteSeatInventoryResponse(@PathVariable long id) {
        seatInventoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
