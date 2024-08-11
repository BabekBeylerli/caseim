package com.example.caseim.service;

import com.example.caseim.dao.entity.ProductPictureEntity;
import com.example.caseim.dao.entity.ProductEntity;
import com.example.caseim.dao.repository.ProductPictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPictureService {
    private final ProductPictureRepository imageRepository;

    @Autowired
    public ProductPictureService(ProductPictureRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String uploadImage(MultipartFile file) {
        try {
            ProductPictureEntity imageEntity = new ProductPictureEntity();
            imageEntity.setImage(file.getBytes());
            ProductPictureEntity savedImage = imageRepository.save(imageEntity);
            return savedImage.getId().toString();
        } catch (IOException e) {
            throw new RuntimeException("Can not uploaded photo");
        }
    }

    public String uploadImage(byte[] file) {
        ProductPictureEntity imageEntity = new ProductPictureEntity();
        imageEntity.setImage(file);
        ProductPictureEntity savedImage = imageRepository.save(imageEntity);
        return savedImage.getId().toString();
    }

    public String uploadImage(byte[] file, ProductEntity foundProduct) {
        ProductPictureEntity imageEntity = new ProductPictureEntity();
        imageEntity.setImage(file);
        imageEntity.setProduct(foundProduct);
        ProductPictureEntity savedImage = imageRepository.save(imageEntity);
        return savedImage.getId().toString();
    }

    public byte[] downloadImage(Integer imageId) {
        return imageRepository.findById(imageId)
                .map(ProductPictureEntity::getImage)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    public List<String> getAllImages() {
        List<String> imageIds = new ArrayList<>();
        for (ProductPictureEntity imageEntity : imageRepository.findAll()) {
            imageIds.add(imageEntity.getId().toString());
        }
        return imageIds;
    }

    public void deleteImage(Integer imageId) {
        imageRepository.deleteById(imageId);
    }
}
