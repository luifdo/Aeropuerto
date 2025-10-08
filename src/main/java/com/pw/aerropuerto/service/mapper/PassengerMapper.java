package com.pw.aerropuerto.service.mapper;


import com.pw.aerropuerto.api.dto.PassangerDtos;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.entities.PassengerProfile;

public class PassengerMapper {
    public static Passenger ToEntity(PassangerDtos.PassengerCreateRequest request){
        var profile = request.profile() == null
                ? null
                : PassengerProfile.builder().phone(request.profile().phone())
                .countryCode(request.profile().countryCode())
                .build();
        return Passenger.builder().name(request.name())
                .email(request.email())
                .profile(profile)
                .build();
    }
    public static PassangerDtos.PassengerResponse toResponse(Passenger passenger) {
        var p = passenger.getProfile();
        var dtoProfile = p == null ? null: new PassangerDtos.PassengerProfileDto(p.getPhone(),  p.getCountryCode());

        return new PassangerDtos.PassengerResponse(passenger.getId(), passenger.getName(), passenger.getEmail(),  dtoProfile);
    }

    public  static void path(Passenger entity, PassangerDtos.PassengerUpdateRequest request){
        if (request.name() != null ) entity.setName(request.name());
        if (request.email() != null) entity.setEmail(request.email());
        if (request.profile() != null){
            var p = entity.getProfile();
            if (p == null){
                p = new PassengerProfile();
                entity.setProfile(p);
            }
            if(request.profile().phone() != null) p.setPhone(request.profile().phone());
            if(request.profile().countryCode() != null) p.setCountryCode(request.profile().countryCode());

        }
    }


}

