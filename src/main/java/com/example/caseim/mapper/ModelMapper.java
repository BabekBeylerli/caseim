package com.example.caseim.mapper;

import com.example.caseim.dao.entity.ModelEntity;
import com.example.caseim.model.ModelDto;
import com.example.caseim.model.ModelLiteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(source = "brandName", target = "brand.name")
    ModelEntity mapLiteDtoToEntity(ModelLiteDto modelDto);

    @Mapping(target = "brandName", source = "brand.name")
    ModelLiteDto mapEntityToLiteDto(ModelEntity modelEntity);

    @Mapping(target = "brandName", source = "brand.name")
    ModelDto mapEntityToDto(ModelEntity modelEntity);

    List<ModelDto> mapEntityToDtos(List<ModelEntity> modelEntities);
}
