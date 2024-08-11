package com.example.caseim.dao.repository;

import com.example.caseim.dao.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ModelRepository extends JpaRepository<ModelEntity, Integer>, JpaSpecificationExecutor<ModelEntity> {
   Optional<ModelEntity>  findByNameIgnoreCase(String name);

}
