package com.example.caseim.controller;


import com.example.caseim.model.BrandDto;
import com.example.caseim.model.BrandLiteDto;
import com.example.caseim.model.BrandPostDto;
import com.example.caseim.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/brands")
public class BrandController {
    private final BrandService brandsService;

    public BrandController(BrandService brandsService) {
        this.brandsService = brandsService;
    }

    @GetMapping
    public List<BrandLiteDto> getAllBrand() {
        return brandsService.getAllBrand();
    }

    @GetMapping("byId/{brandId}")
    public BrandDto getBrand(@PathVariable Integer brandId) {
        return brandsService.getBrand(brandId);
    }

    @GetMapping("byName/{brandName}")
    public BrandPostDto getBrandByName(@PathVariable String brandName) {
        return brandsService.getBrandByName(brandName);
    }

    @PostMapping("/admin")
    public void saveBrand(@RequestBody BrandPostDto brandDto) {
        brandsService.saveBrand(brandDto);
    }

    @DeleteMapping("/admin/{brandId}")
    public void deleteBrand(@PathVariable Integer brandId) {
        brandsService.deleteBrand(brandId);
    }

}
