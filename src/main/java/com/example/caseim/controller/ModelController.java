package com.example.caseim.controller;

import com.example.caseim.model.ModelDto;
import com.example.caseim.model.ModelLiteDto;
import com.example.caseim.service.ModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/models")
public class ModelController {
    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public List<ModelDto> getAllModel() {
        return modelService.getAllModel();
    }

    @GetMapping("/byId/{modelId}")
    public ModelDto getModel(@PathVariable Integer modelId) {
        return modelService.getModel(modelId);
    }

    @GetMapping("/byName/{modelName}")
    public ModelDto getModelByName(@PathVariable String modelName) {
        return modelService.getModelByName(modelName);
    }

    @PostMapping("/admin")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public void saveModel(@RequestBody ModelLiteDto modelDto) {
        modelService.saveModel(modelDto);
    }

    @DeleteMapping("/admin/{modelId}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deleteModel(@PathVariable Integer modelId) {
        modelService.deleteModel(modelId);
    }
}
