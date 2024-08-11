    package com.example.caseim.controller;

    import com.example.caseim.model.ProductPictureDto;
    import com.example.caseim.service.ProductPictureService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.util.Base64;
    import java.util.List;

    @RestController
    @RequestMapping("/test-image")
    public class ProductPictureController {
        private final ProductPictureService imageService;

        public ProductPictureController(ProductPictureService imageService) {
            this.imageService = imageService;
        }

        @PostMapping("/upload")
        public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
            try {
                return new ResponseEntity<>(
                        "Image uploaded successfully: " + imageService.uploadImage(file),
                        HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        "Failed to upload image: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @PostMapping("/upload-byte")
        public ResponseEntity<String> uploadImage(@RequestBody ProductPictureDto dto) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode((dto.getProductPicture()));
                return new ResponseEntity<>(
                        "Image uploaded successfully: " + imageService.uploadImage(imageBytes),
                        HttpStatus.OK);
            } catch (Exception e) {
                System.out.println("Test byte");
                return new ResponseEntity<>("Failed to upload image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @GetMapping("/download/{imageId}")
        public ResponseEntity<byte[]> downloadImage(@PathVariable Integer imageId) {
            byte[] imageData = imageService.downloadImage(imageId);
            if (imageData != null) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageData);
            } else {
                return ResponseEntity.notFound()
                        .build();
            }
        }

        @GetMapping("/getAll")
        public ResponseEntity<List<String>> getAllImages() {
            List<String> imageIds = imageService.getAllImages();
            return new ResponseEntity<>(imageIds, HttpStatus.OK);
        }

        @DeleteMapping("/delete/{imageId}")
        public ResponseEntity<String> deleteImage(@PathVariable Integer imageId) {
            try {
                imageService.deleteImage(imageId);
                return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to delete image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
