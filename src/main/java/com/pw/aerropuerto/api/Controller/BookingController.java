package com.pw.aerropuerto.api.Controller;

import com.pw.aerropuerto.api.dto.BookingDtos.*;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.repositories.PassengerRepository;
import com.pw.aerropuerto.service.BookingService;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private  final BookingService service;
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request,
                                                              UriComponentsBuilder uriComponentsBuilder) {

        Passenger passenger = new Passenger();
        passenger.setId(request.passengerId());

        List<BookItem> bookItems = new ArrayList<>();

        var body = service.create(request,passenger, bookItems);
        var location = uriComponentsBuilder.path("/api/bookings/{id}").buildAndExpand(body.id()).toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getBookings( @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size) {
        var result = service.list(PageRequest.of(page, size, Sort.by("id").ascending()));
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/by-passanger")
    public ResponseEntity<BookingResponse> getByPassenger(@RequestParam("passenger")Long passenger){
        return ResponseEntity.ok(service.get(passenger));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<BookingResponse> update (@PathVariable Long id, @Valid @RequestBody BookingUpdateRequest request){
        return ResponseEntity.ok(service.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BookingResponse> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
