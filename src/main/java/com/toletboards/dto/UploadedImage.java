package com.toletboards.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedImage {

    private String fileName;

    private String storedFileName;

    private String imageUrl;
}