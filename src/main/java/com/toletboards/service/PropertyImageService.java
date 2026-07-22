package com.toletboards.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.toletboards.dto.UploadedImage;

public interface PropertyImageService {

List<UploadedImage> uploadImages(List<MultipartFile> files);

    void deleteImage(String imagePath);

}