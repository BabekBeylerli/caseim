package com.example.caseim.dao.repository;


import com.example.caseim.dao.entity.ProductPictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductPictureRepository extends JpaRepository<ProductPictureEntity, Integer>, JpaSpecificationExecutor<ProductPictureEntity> {
}
