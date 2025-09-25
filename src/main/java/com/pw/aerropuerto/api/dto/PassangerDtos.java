package com.pw.aerropuerto.api.dto;

import java.io.Serializable;
import java.util.Set;

public class PassangerDtos {
    public record PassengerCreateRequest(String name, String email, PassengerProfileDto profile) implements Serializable {
    }
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable{}
    public record PassengerUpdateRequest(String name, String email, PassengerProfileDto profile) implements Serializable{}
    public record PassengerResponse(Long Id, String name, String email, Long passengerProfileId, Set<BookingDtos.BookingResponse> booking, PassengerProfileDto profile) implements Serializable{}
}
