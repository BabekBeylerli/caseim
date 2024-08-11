package com.example.caseim.mapper;


import com.example.caseim.dao.entity.ColorEntity;
import com.example.caseim.dao.entity.ProductPictureEntity;
import com.example.caseim.dao.entity.ProductEntity;
import com.example.caseim.model.ProductDto;
import com.example.caseim.model.ProductDtoForPost;
import com.example.caseim.model.ProductDtoForUpdate;
import com.example.caseim.model.ProductLiteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "modelId", source = "model.id")
    ProductDto mapEntityToDto(ProductEntity productEntity);

    @Mapping(target = "brand.id", source = "brandId")
    @Mapping(target = "model.id", source = "modelId")
    ProductEntity mapDtoToEntity(ProductDto productDto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "viewCount", ignore = true),
            @Mapping(target = "starNumber", ignore = true),
            @Mapping(target = "voteCount", ignore = true),
//            @Mapping(target = "productActiveStatus", ignore = true),
//            @Mapping(target = "favorite", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(source = "brandName", target = "brand.name"),
            @Mapping(source = "modelName", target = "model.name"),
            @Mapping(target = "colors", qualifiedByName = "mapColors"),
            @Mapping(target = "picture", qualifiedByName = "mapImages"),

    })
    ProductEntity mapDtoToEntityForPost(ProductDtoForPost productDtoForPost);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "viewCount", ignore = true),
            @Mapping(target = "starNumber", ignore = true),
            @Mapping(target = "voteCount", ignore = true),
//            @Mapping(target = "productActiveStatus", ignore = true),
//            @Mapping(target = "favorite", ignore = true),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(source = "brandName", target = "brand.name"),
            @Mapping(source = "modelName", target = "model.name"),
            @Mapping(target = "colors", qualifiedByName = "mapColors"),
            @Mapping(target = "picture", qualifiedByName = "mapImages"),

    })
    ProductEntity mapDtoToEntityForUpdate(ProductDtoForUpdate productDtoForUpdate);



    ProductEntity mapDtoToEntity(ProductDto productDto, Integer productId);

    List<ProductDto> mapEntityToDtos(List<ProductEntity> productEntities);

    @Mapping(target = "price", source = "price")
    @Mapping(target = "priceType", source = "priceType")
    @Mapping(target = "starNumber", source = "starNumber")
    @Mapping(target = "modelName", source = "model.name")
    @Mapping(target = "brandName", source = "brand.name")
    ProductLiteDto mapEntityToLiteDto(ProductEntity productEntity);

    @Mapping(target = "price", source = "price")
    @Mapping(target = "priceType", source = "priceType")
    @Mapping(target = "starNumber", source = "starNumber")
    @Mapping(target = "modelName", source = "model.name")
    @Mapping(target = "brandName", source = "brand.name")
    List<ProductLiteDto> mapEntityToLiteDtos2(List<ProductEntity> productEntities);


    @Named("mapImages")
    static List<ProductPictureEntity> mapImages(List<String> images) {
        return images.stream()
                .map(imageUrl -> {
                    ProductPictureEntity imageEntity = new ProductPictureEntity();
                    imageEntity.setImage(imageUrl.getBytes());
                    return imageEntity;
                })
                .collect(Collectors.toList());
    }


    @Named("mapColors")
    static Set<ColorEntity> mapStringListToColorEntitySet(List<String> colors) {
        Set<ColorEntity> colorEntities = new HashSet<>();

        if (colors != null) {
            for (String color : colors) {
                ColorEntity colorEntity = new ColorEntity();
                colorEntity.setName(color);
                colorEntities.add(colorEntity);
            }
        }

        return colorEntities;
    }

}