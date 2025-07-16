package com.example.blog.services;

import com.example.blog.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final List<String> allowedTypes = List.of("image/png", "image/jpeg");

    @Override
    public String uploadImage(MultipartFile file){
        String contentType = file.getContentType();

        if(!allowedTypes.contains(contentType)){
            throw new InvalidInputException("Only png and jpg files are allowed");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = (Path) Paths.get(uploadDir).resolve(fileName);

        try{
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e){
            throw new InvalidInputException("Failed to upload file: " + e.getMessage());
        }

        return fileName;
    }
}
