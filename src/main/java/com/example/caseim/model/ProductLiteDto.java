package com.example.caseim.model;

import com.example.caseim.dao.entity.ProductPictureEntity;
import com.example.caseim.dao.entity.enums.PriceType;
import com.example.caseim.dao.entity.ProductPictureEntity;
import com.example.caseim.dao.entity.enums.PriceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductLiteDto {
    private Integer id;
    private Double price;
    private PriceType priceType;
    private Integer starNumber;
    private List<ProductPictureEntity> productPicture;
    private String modelName;
    private String brandName;

}
