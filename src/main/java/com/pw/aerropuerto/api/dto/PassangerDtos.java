package com.pw.aerropuerto.api.dto;

import java.io.Serializable;

public class PassangerDtos {
    public record PassengerCreateRequest(String name, String email, PassengerProfileDto profile) implements Serializable {
    }
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable{}
    public record PassengerUpdateRequest(String name, String email, PassengerProfileDto profile) implements Serializable{}
    public record PassengerResponse(Long Id, String name, String email, PassengerProfileDto profile) implements Serializable{}
}
