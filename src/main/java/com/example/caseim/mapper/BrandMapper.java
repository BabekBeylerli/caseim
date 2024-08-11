package com.example.caseim.mapper;


import com.example.caseim.dao.entity.BrandEntity;
import com.example.caseim.model.BrandDto;
import com.example.caseim.model.BrandLiteDto;
import com.example.caseim.model.BrandPostDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandPostDto mapEntityToDto2(BrandEntity brandEntity);
    BrandEntity mapDtoToEntity2(BrandPostDto brandDto);
    BrandEntity mapDtoToEntity(BrandDto brandDto, Integer brandId);
    List<BrandDto> mapEntityToDtos(List<BrandEntity> brandEntities);
    List<BrandLiteDto> mapEntityToDtos2(List<BrandEntity> brandEntities);
    BrandDto mapEntityToDto(BrandEntity brandEntity);
    BrandEntity mapDtoToEntity(BrandDto brandDto);
}