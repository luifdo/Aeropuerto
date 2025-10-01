package com.pw.aerropuerto.service.mapper;


import com.pw.aerropuerto.api.dto.TagDtos;
import com.pw.aerropuerto.dominio.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TagMapper {

    @Mapping(source = "", target = "")


    TagDtos tagToTagdtos(Tag tag);

}
