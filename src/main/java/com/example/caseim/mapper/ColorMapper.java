package com.example.caseim.mapper;

import com.example.caseim.dao.entity.ColorEntity;
import com.example.caseim.model.ColorDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    ColorDto mapEntityToDto(ColorEntity colorEntity);
    ColorEntity mapDtoToEntity(ColorDto colorDto);
    ColorEntity mapDtoToEntity(ColorDto colorDto, Integer colorId);
    List<ColorDto> mapEntityToDtos(List<ColorEntity> colorEntities);

}