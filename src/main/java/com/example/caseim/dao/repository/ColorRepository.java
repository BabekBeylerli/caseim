package com.example.caseim.dao.repository;

import com.example.caseim.dao.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository  extends JpaRepository<ColorEntity, Integer>, JpaSpecificationExecutor<ColorEntity> {
    ColorEntity findByNameIgnoreCase(String name);

}
