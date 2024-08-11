package com.example.caseim.service;

import com.example.caseim.dao.entity.BrandEntity;
import com.example.caseim.dao.entity.ColorEntity;
import com.example.caseim.dao.entity.ModelEntity;
import com.example.caseim.dao.entity.ProductEntity;
import com.example.caseim.dao.repository.BrandRepository;
import com.example.caseim.dao.repository.ColorRepository;
import com.example.caseim.dao.repository.ModelRepository;
import com.example.caseim.dao.repository.ProductRepository;
import com.example.caseim.mapper.ProductMapper;
import com.example.caseim.model.*;
import com.example.caseim.service.specification.ProductSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final ColorRepository colorRepository;
    private final ProductPictureService productPictureService;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, BrandRepository brandRepository, ModelRepository modelRepository, ColorRepository colorRepository, ProductPictureService productPictureService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
        this.colorRepository = colorRepository;

        this.productPictureService = productPictureService;
    }

    public Page<ProductLiteDto> getAllProductByFilter(Pageable pageable, ProductFilterDto productFilterDto) {
        Specification<ProductEntity> specification = new ProductSpecification(productFilterDto);

        // Tarihe göre azalan sıralama
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));

        Pageable pageableWithSorting = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        Page<ProductEntity> productPage = productRepository.findAll(specification, pageableWithSorting);
        List<ProductLiteDto> productLiteDtos = productMapper.mapEntityToLiteDtos2(productPage.getContent());

        return new PageImpl<>(productLiteDtos, pageable, productPage.getTotalElements());
    }

    public List<ProductLiteDto> getAllProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        return productMapper.mapEntityToLiteDtos2(productPage.getContent());
    }

    public void addStarRatingToProduct(Integer productId, int newStarRating) {
        ProductEntity productEntity = productRepository.findById(productId).orElse(null);

        if (productEntity != null) {
            int totalVotes = (productEntity.getStarNumber() != null ? productEntity.getStarNumber() : 0) *
                    (productEntity.getVoteCount() != null ? productEntity.getVoteCount() : 0);

            totalVotes += newStarRating;
            productEntity.setVoteCount((productEntity.getVoteCount() != null ? productEntity.getVoteCount() : 0) + 1);

            if (productEntity.getVoteCount() != 0) {
                productEntity.setStarNumber(totalVotes / productEntity.getVoteCount());
            } else {
                productEntity.setStarNumber(0);
            }

            productRepository.save(productEntity);
        } else {
            log.error("Product with id {} not found.", productId);
        }
    }

    public Double getAverageStarRating(Integer productId) {
        ProductEntity productEntity = productRepository.findById(productId).orElse(null);

        if (productEntity != null && productEntity.getStarNumber() != null) {
            return productEntity.getStarNumber().doubleValue();
        } else {
            log.error("Product with id {} not found or starNumber is null.", productId);
            return null;
        }
    }

    @Transactional
    public ProductDto getProduct(Integer productId) {
        log.info("ActionLog.getProduct.start");
        ProductEntity productEntity =
                productRepository.findById(productId).orElseThrow(() ->
                        new RuntimeException("Not Found!")
                );

        Integer currentViewNumber = productEntity.getViewCount();

        productEntity.setViewCount(currentViewNumber != null ? currentViewNumber + 1 : 1);

        log.info("ActionLog.getProduct.end");
        return productMapper.mapEntityToDto(productEntity);
    }


    @Transactional
    public void saveProduct(ProductDtoForPost productDto) {
        log.info("ActionLog.saveProduct.start");

        String caseCode = generateUniqueCaseCode();

        BrandEntity brandEntity = brandRepository.findByNameIgnoreCase(productDto.getBrandName());
        if (brandEntity == null) {
            brandEntity = new BrandEntity();
            brandEntity.setName(productDto.getBrandName());
            brandRepository.save(brandEntity); // BrandEntity'yi kaydet
        }

        Optional<ModelEntity> modelEntityOptional = modelRepository.findByNameIgnoreCase(productDto.getModelName());
        ModelEntity modelEntity;
        if (modelEntityOptional.isPresent()) {
            modelEntity = modelEntityOptional.get();
        } else {
            // Veritabanında bulunmuyorsa yeni bir ModelEntity oluştur
            modelEntity = new ModelEntity();
            modelEntity.setName(productDto.getModelName());
            // BrandEntity ile ilişkilendirme
            modelEntity.setBrand(brandEntity); // Yeni oluşturulan BrandEntity'yi bağla
            // Diğer özellikleri set edebilirsiniz.
            modelEntity = modelRepository.save(modelEntity); // ModelEntity'yi kaydet
        }

        // Color kontrolü
        Set<ColorEntity> colorEntities = new HashSet<>();
        if (productDto.getColors() != null) {
            for (String color : productDto.getColors()) {
                ColorEntity colorEntity = colorRepository.findByNameIgnoreCase(color);
                if (colorEntity == null) {
                    // Veritabanında bulunmuyorsa yeni bir ColorEntity oluştur
                    colorEntity = new ColorEntity();
                    colorEntity.setName(color);
                    // Diğer özellikleri set edebilirsiniz.
                    colorRepository.save(colorEntity); // ColorEntity'yi kaydet
                }
                colorEntities.add(colorEntity);
            }
        }

        // ProductEntity oluştur
        ProductEntity productEntity = productMapper.mapDtoToEntityForPost(productDto);
        productEntity.setBrand(brandEntity);
        productEntity.setModel(modelEntity);
        productEntity.setColors(colorEntities);
        productEntity.setCaseCode(caseCode); // Set the generated case code

        // ProductEntity'yi kaydet
        ProductEntity savedProductEntity = productRepository.save(productEntity);
        ProductEntity foundProductEntity = productRepository.findById(savedProductEntity.getId()).orElse(null);

        List<String> images = productDto.getPicture();
        for (String image : images) {
            byte[] imageBytes = Base64.getDecoder().decode(image.getBytes());
            productPictureService.uploadImage(imageBytes, foundProductEntity);
        }

        log.info("ActionLog.saveProduct.end");
    }


    @Transactional
    public void editProduct(ProductDtoForUpdate productDto, Integer productId) {
        // productId ile var olan ProductEntity'yi alın
        Optional<ProductEntity> existingProductOptional = productRepository.findById(productId);

        if (existingProductOptional.isPresent()) {
            ProductEntity existingProduct = existingProductOptional.get();

            // ProductDtoForUpdate'u ProductEntity'ye dönüştür
            ProductEntity updatedProduct = productMapper.mapDtoToEntityForUpdate(productDto);

            // Renkleri kontrol et ve gerekirse ekleyip referans yap
            Set<ColorEntity> existingColors = existingProduct.getColors();
            Set<ColorEntity> updatedColors = updatedProduct.getColors();

            updatedColors.stream()
                    .filter(color -> !existingColors.contains(color))
                    .forEach(color -> {
                        ColorEntity existingColor = colorRepository.findByNameIgnoreCase(color.getName());
                        if (existingColor != null) {
                            existingColors.add(existingColor);
                        } else {
                            existingColors.add(color);
                            colorRepository.save(color);
                        }
                    });

            // Marka kontrol et ve gerekirse ekleyip referans yap
            BrandEntity updatedBrand = updatedProduct.getBrand();
            if (updatedBrand != null) {
                Optional<BrandEntity> existingBrandOptional = brandRepository.findByName(updatedBrand.getName());
                if (existingBrandOptional.isPresent()) {
                    existingProduct.setBrand(existingBrandOptional.get());
                } else {
                    updatedBrand.setId(null);
                    existingProduct.setBrand(updatedBrand);
                    brandRepository.save(updatedBrand);
                }
            }

            // Model kontrol et ve gerekirse ekleyip referans yap
            ModelEntity updatedModel = updatedProduct.getModel();
            if (updatedModel != null) {
                Optional<ModelEntity> existingModelOptional = modelRepository.findByNameIgnoreCase(updatedModel.getName());
                if (existingModelOptional.isPresent()) {
                    existingProduct.setModel(existingModelOptional.get());
                } else {
                    updatedModel.setId(null);
                    existingProduct.setModel(updatedModel);
                    modelRepository.save(updatedModel);
                }
            }

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCaseCode(updatedProduct.getCaseCode());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setOpportunities(updatedProduct.getOpportunities());
            existingProduct.setPriceType(updatedProduct.getPriceType());

            productRepository.saveAndFlush(existingProduct);
        } else {
            throw new EntityNotFoundException("Product with id " + productId + " not found");
        }
    }

    public void deleteProduct(Integer productId) {
        log.info("ActionLog.deleteProduct.start");
        productRepository.deleteById(productId);
        log.info("ActionLog.deleteProduct.end");
    }

    private String generateUniqueCaseCode() {
        String prefix = "CASE";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp;
    }


//    //default
//    private static final double CUSTOM_PRODUCT_PRICE = 15.0;
//    @Transactional
//    public ProductEntity createCustomProduct(OrderDto orderDto) {
//        ProductDtoForPost productDto = new ProductDtoForPost();
//        productDto.setName("Custom Product for Order " + orderDto.getUserId());
//        productDto.setPrice(CUSTOM_PRODUCT_PRICE);  // Özel dizayn ürünlerin başlangıç fiyatı
//        productDto.setDescription("This is a custom product created for an order.");
//        // Diğer gerekli alanları doldurabilirsiniz.
//
//        ProductEntity productEntity = new ProductEntity();
//        productEntity.setName(productDto.getName());
//        productEntity.setPrice(productDto.getPrice());
//        productEntity.setDescription(productDto.getDescription());
//        // Diğer alanları setleyin.
//
//        return productRepository.save(productEntity);
//    }

    // Default ürün kontrolü ve alma
    @Transactional
    public ProductEntity getDefaultProduct(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Default product not found"));
    }


}
