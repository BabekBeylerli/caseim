package com.example.caseim.service;

import com.example.caseim.dao.entity.BrandEntity;
import com.example.caseim.dao.entity.ModelEntity;
import com.example.caseim.dao.repository.BrandRepository;
import com.example.caseim.dao.repository.ModelRepository;
import com.example.caseim.mapper.ModelMapper;
import com.example.caseim.model.ModelDto;
import com.example.caseim.model.ModelLiteDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ModelService {
    private final ModelRepository modelRepository;
    private final ModelMapper mapper;
    private final BrandRepository brandRepository;

    public ModelService(ModelRepository modelRepository, ModelMapper mapper, BrandRepository brandRepository) {
        this.modelRepository = modelRepository;
        this.mapper = mapper;
        this.brandRepository = brandRepository;
    }

    public List<ModelDto> getAllModel() {
        log.info("ActionLog.getAllModel.start");
        List<ModelDto> modelDtos;
        try {
            List<ModelEntity> modelEntities = modelRepository.findAll();
            modelDtos = mapper.mapEntityToDtos(modelEntities);
            log.info("ActionLog.getAllModel.end");
        } catch (Exception e) {
            log.error("Error in getAllModel: ", e);
            throw new RuntimeException("An error occurred while fetching all models.", e);
        }
        return modelDtos;
    }

    public ModelDto getModel(Integer modelId) {
        log.info("ActionLog.getModel.start");
        ModelDto modelDto;
        try {
            ModelEntity modelEntity = modelRepository.findById(modelId)
                    .orElseThrow(() -> new RuntimeException("Model not found"));
            modelDto = mapper.mapEntityToDto(modelEntity);
            log.info("ActionLog.getModel.end");
        } catch (Exception e) {
            log.error("Error in getModel: ", e);
            throw new RuntimeException("An error occurred while fetching the model.", e);
        }
        return modelDto;
    }

    public ModelDto getModelByName(String name) {
        log.info("ActionLog.getModelByName.start");
        ModelDto modelDto;
        try {
            Optional<ModelEntity> modelEntityOptional = modelRepository.findByNameIgnoreCase(name);
            if (modelEntityOptional.isPresent()) {
                ModelEntity modelEntity = modelEntityOptional.get();
                modelDto = mapper.mapEntityToDto(modelEntity);
                log.info("ActionLog.getModelByName.end");
            } else {
                throw new RuntimeException("Model not found");
            }
        } catch (Exception e) {
            log.error("Error in getModelByName: ", e);
            throw new RuntimeException("An error occurred while fetching the model by name.", e);
        }
        return modelDto;
    }

    @Transactional
    public void saveModel(ModelLiteDto modelDto) {
        log.info("ActionLog.saveModel.start");

        try {

            Optional<ModelEntity> existingModelOptional = modelRepository.findByNameIgnoreCase(modelDto.getName());
            if (existingModelOptional.isPresent()) {
                throw new IllegalArgumentException("Model with name " + modelDto.getName() + " already exists");
            }

            BrandEntity brandEntity = brandRepository.findByNameIgnoreCase(modelDto.getBrandName());
            if (brandEntity == null) {
                brandEntity = new BrandEntity();
                brandEntity.setName(modelDto.getBrandName());
                brandEntity = brandRepository.save(brandEntity);
            }

            ModelEntity modelEntity = mapper.mapLiteDtoToEntity(modelDto);
            modelEntity.setBrand(brandEntity);

            modelRepository.save(modelEntity);

            log.info("ActionLog.saveModel.end");
        } catch (Exception e) {
            log.error("Error in saveModel: ", e);
            throw new RuntimeException("An error occurred while saving the model.", e);
        }
    }

    public void deleteModel(Integer modelId) {
        log.info("ActionLog.deleteModel.start");
        try {
            if (!modelRepository.existsById(modelId)) {
                throw new RuntimeException("Model not found");
            }
            modelRepository.deleteById(modelId);
            log.info("ActionLog.deleteModel.end");
        } catch (Exception e) {
            log.error("Error in deleteModel: ", e);
            throw new RuntimeException("An error occurred while deleting the model.", e);
        }
    }
}
