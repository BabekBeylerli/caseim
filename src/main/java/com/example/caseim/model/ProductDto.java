package com.example.caseim.model;


import com.example.caseim.dao.entity.ColorEntity;
import com.example.caseim.dao.entity.ProductPictureEntity;
import com.example.caseim.dao.entity.enums.PriceType;
import com.example.caseim.validation.Capitalized;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDto {
    @NotBlank(message = "Brand name cannot be blank")
    @Size(min = 2, max = 30)
    @Capitalized(message = "First letter of first name must be capitalized")
    private String name;
    private Double price;
    private Integer viewCount;
    private String caseCode;
    private String opportunities;
    private String description;
    private Integer starNumber;
    private PriceType priceType;
//    private ProductActiveStatus productActiveStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductPictureEntity> productPicture;
//    private List<FavoriteEntity> favorite;
    private List<ColorEntity> colors;
    private Integer modelId;
    private Integer brandId;
//    private Integer shopId;
}