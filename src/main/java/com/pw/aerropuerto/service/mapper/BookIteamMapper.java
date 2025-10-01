package com.pw.aerropuerto.service.mapper;


import com.pw.aerropuerto.api.dto.BookItemDtos.*;
import com.pw.aerropuerto.dominio.entities.BookItem;
import com.pw.aerropuerto.dominio.entities.Cabin;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper (componentModel = "Spring")
public interface BookIteamMapper {

    BookIteamMapper instance = Mappers.getMapper(BookIteamMapper.class);

    @Mapping(target = "cabin", expression = "java(cabinFromString(request.cabinType()))")
    @Mapping(target = "booking", expression = "java(new Booking(request.bookingId()))")
    @Mapping(target = "flight", expression = "java(new Flight(request.flightId()))")
    BookItem toEntity(BookItemCreateRequest request);

    @Mapping(target = "bookingId", expression = "java(bookItem.getBooking() != null ? bookItem.getBooking().getId() : null)")
    @Mapping(target = "flightId", expression = "java(bookItem.getFlight() != null ? bookItem.getFlight().getId() : null)")
    BookItemResponse toResponse(BookItem bookItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cabin", expression = "java(cabinFromString(request.cabinType()))")
    void patch(@MappingTarget BookItem entity, BookItemUpdateRequest request);

    default Cabin cabinFromString(String cabinType) {
        if (cabinType == null) {
            return null;
        }
        return Cabin.valueOf(cabinType.toUpperCase());

    }

}
