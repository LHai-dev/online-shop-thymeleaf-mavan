package com.onlineshopthymeleaf.util;

import com.onlineshopthymeleaf.model.FileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUtil {

    @Value("${server.path}")
    private String serverPath;

    @Value("${file.base.url}")
    private String fileBaseUrl;

    public FileDto upload(MultipartFile file) {
        String extension = getExtension(file.getOriginalFilename());
        if (extension == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        long size = file.getSize();
        String name = String.format("%s.%s", UUID.randomUUID(), extension);  // e.g., f54a78b8-7179-4a01-9f7b-b00e7f27e7a5.png
        String url = String.format("%s%s", fileBaseUrl, name);  // e.g., http://localhost:8080/files/f54a78b8-7179-4a01-9f7b-b00e7f27e7a5.png
        Path path = Paths.get(serverPath + name);  // e.g., src/main/resources/file/f54a78b8-7179-4a01-9f7b-b00e7f27e7a5.png

        try {
            Files.copy(file.getInputStream(), path);
            return FileDto.builder()
                    .fileName(name)
                    .fileLocation(url)
                    .extension(extension)
                    .size(size)
                    .build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "UPLOAD_FILE_FAILED");
        }
    }

    public Resource findByName(String name) {
        Path path = Paths.get(serverPath + name);
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FILE_NOT_FOUND");
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public FileDto findFileByName(String name) throws IOException {
        Resource resource = findByName(name);
        return FileDto.builder()
                .fileName(resource.getFilename())
                .extension(getExtension(resource.getFilename()))
                .fileLocation(String.format("%s%s", fileBaseUrl, resource.getFilename()))
                .size(resource.contentLength())
                .build();
    }

    public void deleteByName(String name) {
        Path path = Paths.get(serverPath + name);
        try {
            boolean isDeleted = Files.deleteIfExists(path);
            if (!isDeleted) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FILE_NOT_FOUND");
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DELETE_FILE_FAILED");
        }
    }

    private String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }
}
