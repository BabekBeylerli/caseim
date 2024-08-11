package com.example.caseim.service;


import com.example.caseim.dao.entity.ColorEntity;
import com.example.caseim.dao.repository.ColorRepository;
import com.example.caseim.mapper.ColorMapper;
import com.example.caseim.model.ColorDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ColorService {
    private final ColorRepository colorRepository;
    private final ColorMapper colorMapper;

    public ColorService(ColorRepository colorRepository, ColorMapper colorMapper) {
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
    }

    public List<ColorDto> getAllColor() {
        log.info("ActionLog.getAllColor.start");
        List<ColorDto> colorDtos = colorMapper.mapEntityToDtos(colorRepository.findAll());
        log.info("ActionLog.getAllColor.end");
        return colorDtos;
    }
    public ColorDto getColor(Integer colorId) {
        log.info("ActionLog.getModel.start");
        ColorEntity colorEntity =
                colorRepository.findById(colorId).orElseThrow(() ->
                        new RuntimeException("Not Found")
                );
        log.info("ActionLog.getModel.end");
        return colorMapper.mapEntityToDto(colorEntity);
    }
    public ColorDto getColorByName(String name){
        return colorMapper.mapEntityToDto(colorRepository.findByNameIgnoreCase(name));
    }
    @Transactional
    public void saveColor(ColorDto colorDto) {
        log.info("ActionLog.saveColor.start");

        ColorEntity existingColor = colorRepository.findByNameIgnoreCase(colorDto.getName());
        if (existingColor != null) {
            throw new IllegalArgumentException("Color with name " + colorDto.getName() + " already exists");
        }

        ColorEntity colorEntity = colorMapper.mapDtoToEntity(colorDto);

        colorRepository.save(colorEntity);

        log.info("ActionLog.saveColor.end");
    }

    public void deleteColor(Integer colorId) {
        log.info("ActionLog.deleteModel.start");
        colorRepository.deleteById(colorId);
        log.info("ActionLog.deleteModel.end");
    }
}
