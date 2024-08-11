package com.example.caseim.controller;

import com.example.caseim.model.*;
import com.example.caseim.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/filter")
    public Page<ProductLiteDto> getAllProductByFilter(Pageable pageable, ProductFilterDto productFilterDto) {
        return productService.getAllProductByFilter(pageable, productFilterDto);
    }

    @GetMapping("/public")
    public List<ProductLiteDto> getAllProduct(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return productService.getAllProduct(page, size);
    }

    @PostMapping("/{productId}/rate")
    public ResponseEntity<String> addStarRatingToProduct(
            @PathVariable Integer productId,
            @RequestParam int newStarRating
    ) {
        productService.addStarRatingToProduct(productId, newStarRating);
        return ResponseEntity.ok("Star rating added successfully.");
    }

    @GetMapping("/{productId}/average-rating")
    public ResponseEntity<Double> getAverageStarRating(@PathVariable Integer productId) {
        Double averageRating = productService.getAverageStarRating(productId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/public/{productId}")
    public ProductDto getProduct(@PathVariable Integer productId) {
        return productService.getProduct(productId);
    }

    @PostMapping("/user")
//    @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public void saveProduct(@RequestBody ProductDtoForPost productDto) {
        productService.saveProduct(productDto);
    }
    @PutMapping("/user/{productId}")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void updateProduct(@RequestBody ProductDtoForUpdate productDto, @PathVariable Integer productId) {
        productService.editProduct(productDto, productId);
    }
    @DeleteMapping("/user/{productId}")
//    @PreAuthorize("hasAnyRole('SHOP','ADMIN')")
    public void deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
    }
}
