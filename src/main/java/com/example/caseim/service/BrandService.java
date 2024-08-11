package com.example.caseim.service;

import com.example.caseim.dao.entity.BrandEntity;
import com.example.caseim.dao.repository.BrandRepository;
import com.example.caseim.mapper.BrandMapper;
import com.example.caseim.model.BrandDto;
import com.example.caseim.model.BrandLiteDto;
import com.example.caseim.model.BrandPostDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
public class BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public BrandService(BrandRepository brandsRepository, BrandMapper brandMapper) {
        this.brandRepository = brandsRepository;
        this.brandMapper = brandMapper;
    }

    public List<BrandLiteDto> getAllBrand() {
        log.info("ActionLog.getAllBrands.start");
        List<BrandLiteDto> brandLiteDtos = brandMapper.mapEntityToDtos2(brandRepository.findAll());
        log.info("ActionLog.getAllBrands.end");
        return brandLiteDtos;
    }

    public BrandDto getBrand(Integer brandId) {
        log.info("ActionLog.getBrand.start");
        BrandEntity brandEntity =
                brandRepository.findById(brandId).orElseThrow(() ->
                        new RuntimeException("Not Found")

                );
        log.info("ActionLog.getBrand.end");
        return brandMapper.mapEntityToDto(brandEntity);
    }
    public BrandPostDto getBrandByName(String name){
        return brandMapper.mapEntityToDto2(brandRepository. findByNameIgnoreCase(name));
    }
    @Transactional
    public void saveBrand(BrandPostDto brandDto) {
        log.info("ActionLog.saveBrand.start");

        String brandName = brandDto.getName();
        if (brandRepository.findByName(brandName).isPresent()) {
            log.error("Brand with name '{}' already exists", brandName);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Brand with name '" + brandName + "' already exists");
        }

        brandRepository.save(brandMapper.mapDtoToEntity2(brandDto));
        log.info("ActionLog.saveBrand.end");
    }

    public void deleteBrand(Integer brandId) {
        log.info("ActionLog.deleteBrand.start");
        brandRepository.deleteById(brandId);
        log.info("ActionLog.deleteBrand.end");
    }

}
