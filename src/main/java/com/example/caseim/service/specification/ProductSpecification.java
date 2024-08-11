package com.example.caseim.service.specification;


import com.example.caseim.dao.entity.ProductEntity;
import com.example.caseim.model.ProductFilterDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification implements Specification<ProductEntity> {
    private final ProductFilterDto productFilterDto;

    public ProductSpecification(ProductFilterDto productFilterDto) {
        this.productFilterDto = productFilterDto;
    }


    @Override
    public Predicate toPredicate(Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        // Qiymet araligi filtresi
        if (productFilterDto.getUpPrice() != null && productFilterDto.getDownPrice() != null) {
            predicates.add(criteriaBuilder.between(root.get("price"), productFilterDto.getDownPrice(), productFilterDto.getUpPrice()));
        }

        // Mehsul adi  filtresi (contains)
        if (productFilterDto.getName() != null && !productFilterDto.getName().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + productFilterDto.getName() + "%"));
        }

        // Fursatlar filtresi (contains)
        if (productFilterDto.getOpportunities() != null && !productFilterDto.getOpportunities().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("opportunities"), "%" + productFilterDto.getOpportunities() + "%"));
        }

        // Ulduz sayisi filtresi
        if (productFilterDto.getStarNumber() != null) {
            predicates.add(criteriaBuilder.equal(root.get("starNumber"), productFilterDto.getStarNumber()));
        }

        // Tarix aralığı filtresi (createdAt)
        if (productFilterDto.getDownDate() != null && productFilterDto.getUpDate() != null) {
            LocalDateTime downDate = productFilterDto.getDownDate().with(LocalTime.MIN);
            LocalDateTime upDate = productFilterDto.getUpDate().with(LocalTime.MAX);

            predicates.add(criteriaBuilder.between(root.get("createdAt"), downDate, upDate));
        }


        // Combine predicates with AND
        if (!predicates.isEmpty()) {
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }

        return null;
    }
}

