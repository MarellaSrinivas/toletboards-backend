package com.toletboards.service.impl;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
 import com.toletboards.dto.UploadedImage;
import com.toletboards.service.PropertyImageService;

@Service
public class PropertyImageServiceImpl implements PropertyImageService {

    private static final String UPLOAD_DIR = "uploads/properties/";

    @Override
    public List<UploadedImage> uploadImages(List<MultipartFile> files) {

        List<UploadedImage> uploadedImages = new ArrayList<>();

        try {

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if (files == null) {
                return uploadedImages;
            }

            for (MultipartFile file : files) {

                if (file == null || file.isEmpty()) {
                    continue;
                }

                String originalName = file.getOriginalFilename();

                if (originalName == null) {
                    originalName = "image";
                }

                String extension = "";

                if (originalName.contains(".")) {
                    extension = originalName.substring(originalName.lastIndexOf("."));
                }

                String storedFileName =
                        UUID.randomUUID() + extension;

                Path destination =
                        uploadPath.resolve(storedFileName);

                Files.copy(
                        file.getInputStream(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING
                );

                uploadedImages.add(

                        UploadedImage.builder()

                                .fileName(originalName)

                                .storedFileName(storedFileName)

                                .imageUrl("/uploads/properties/" + storedFileName)

                                .build()

                );

            }

        } catch (IOException e) {

            throw new RuntimeException("Unable to upload images", e);

        }

        return uploadedImages;
    }

    @Override
    public void deleteImage(String imagePath) {

        try {

            if (imagePath == null) {
                return;
            }

            String fileName =
                    imagePath.replace("/uploads/properties/", "");

            Path path =
                    Paths.get(UPLOAD_DIR).resolve(fileName);

            Files.deleteIfExists(path);

        } catch (IOException e) {

            throw new RuntimeException("Unable to delete image", e);

        }
    }
}