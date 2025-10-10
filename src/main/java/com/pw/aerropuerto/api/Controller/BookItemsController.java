package com.pw.aerropuerto.api.Controller;

import com.pw.aerropuerto.api.dto.BookItemDtos.*;
import com.pw.aerropuerto.dominio.entities.Flight;
import com.pw.aerropuerto.service.BookingItemService;
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
@RequestMapping("/api/bookItems")
@RequiredArgsConstructor
@Validated
public class BookItemsController {
    private final BookingItemService service;
    private  Flight flight;
    @PostMapping
    public ResponseEntity<BookItemResponse> create (@Valid@RequestBody BookItemCreateRequest req,
                                                    UriComponentsBuilder uriComponentsBuilder){
        var body = service.create(req);
        var location = uriComponentsBuilder.path("/api/bookItems/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }
    @GetMapping
    private ResponseEntity<Page<BookItemResponse>> list( @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size){
        var result = service.list(PageRequest.of(page, size, Sort.by("id").ascending()));
        return ResponseEntity.ok(result);
    }
    @GetMapping("/by-flight")
    public ResponseEntity<BookItemResponse> getByFlight(@RequestParam("flight") Long flight){
        return ResponseEntity.ok(service.get(flight));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<BookItemResponse> update(@PathVariable Long id,
                                                   @Valid@RequestBody BookItemUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
