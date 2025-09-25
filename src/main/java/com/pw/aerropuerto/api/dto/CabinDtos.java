package com.pw.aerropuerto.api.dto;

import com.pw.aerropuerto.dominio.entities.Cabin;

import java.io.Serializable;

public class CabinDtos {
    public record CabinRequest(Cabin cabin) implements Serializable {}
    public record CabinResponse(Cabin cabin) implements Serializable {}
}
