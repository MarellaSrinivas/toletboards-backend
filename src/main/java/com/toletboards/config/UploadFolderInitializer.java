package com.toletboards.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UploadFolderInitializer
        implements CommandLineRunner {

    private final FileUploadProperties properties;

    @Override
    public void run(String... args) throws Exception {

        Path uploadPath = Paths.get(properties.getUploadDir());

        try {

            Files.createDirectories(uploadPath);

            System.out.println("--------------------------------");
            System.out.println("Upload Folder Ready");
            System.out.println(uploadPath.toAbsolutePath());
            System.out.println("--------------------------------");

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not create upload directory",
                    e);

        }

    }

}