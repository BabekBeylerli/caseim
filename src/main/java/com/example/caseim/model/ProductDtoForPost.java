package com.example.caseim.model;

import com.example.caseim.dao.entity.enums.PriceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDtoForPost {
    private String name;
    private Double price;
    private String caseCode;
    private String description;
    private String opportunities;
    private PriceType priceType;
    private List<String> picture;
    private String brandName;
    private String modelName;
    private List<String> colors;
    private LocalDateTime createdAt;



}