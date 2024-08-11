package com.example.caseim.mapper;

import com.example.caseim.dao.entity.ProductPictureEntity;
import com.example.caseim.model.ProductPictureDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPictureMapper {
    ProductPictureDto mapEntityToDto(ProductPictureEntity entity);

    ProductPictureEntity mapDtoToEntity(ProductPictureDto dto);

    List<ProductPictureDto> mapEntityToDtos(List<ProductPictureEntity> entities);
}