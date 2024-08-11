package com.example.caseim.controller;

import com.example.caseim.model.ColorDto;
import com.example.caseim.service.ColorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/colors")
public class ColorController {
    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public List<ColorDto> getAllColor() {
        return colorService.getAllColor();
    }

    @GetMapping("byId/{colorId}")
    public ColorDto getColor(@PathVariable Integer colorId) {
        return colorService.getColor(colorId);
    }

    @GetMapping("byName/{colorName}")
    public ColorDto getColorByName(@PathVariable String colorName) {
        return colorService.getColorByName(colorName);
    }

    @PostMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
    public void saveColor(@RequestBody ColorDto colorDto) {
        colorService.saveColor(colorDto);
    }

    @DeleteMapping("/admin/{colorId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public void deleteColor(@PathVariable Integer colorId) {
        colorService.deleteColor(colorId);
    }
}
