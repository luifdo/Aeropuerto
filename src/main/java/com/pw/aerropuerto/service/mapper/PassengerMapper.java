package com.pw.aerropuerto.service.mapper;

import com.pw.aerropuerto.api.dto.PassengerDtos.*;
import com.pw.aerropuerto.dominio.entities.Passenger;
import com.pw.aerropuerto.dominio.entities.PassengerProfile;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    PassengerMapper INSTANCE = Mappers.getMapper(PassengerMapper.class);

    @Mapping(target = "profile", source = "profile")
    Passenger toEntity(PassengerCreateRequest request);

    @Mapping(target = "profile", source = "profile")
    PassengerResponse toResponse(Passenger passenger);

    @Mapping(target = "profile", source = "profile")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(@MappingTarget Passenger entity, PassengerUpdateRequest request);

    // Mappers para PassengerProfile y su DTO
    PassengerProfile map(PassengerProfileDto dto);

    PassengerProfileDto map(PassengerProfile profile);

    // Manejo especial para patch de profile
    @AfterMapping
    default void patchProfile(@MappingTarget Passenger entity, PassengerUpdateRequest request) {
        if (request.profile() != null) {
            PassengerProfile profile = entity.getPassengerProfile();
            if (profile == null) {
                profile = new PassengerProfile();
                entity.setPassengerProfile(profile);
            }
            PassengerProfileDto dto = request.profile();
            if (dto.phone() != null) {
                profile.setPhone(dto.phone());
            }
            if (dto.countryCode() != null) {
                profile.setCountryCode(dto.countryCode());
            }
        }
    }
}
