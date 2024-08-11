package com.example.caseim.dao.repository;


import com.example.caseim.dao.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer>, JpaSpecificationExecutor<BrandEntity> {
    BrandEntity  findByNameIgnoreCase(String name);
    Optional<BrandEntity> findByName(String name);
}

