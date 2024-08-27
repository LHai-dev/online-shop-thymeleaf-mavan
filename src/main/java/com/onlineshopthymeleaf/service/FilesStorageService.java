package com.onlineshopthymeleaf.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {

    String save(MultipartFile file) throws IOException;

   Resource load(String filename);

   boolean delete(String filename);
  

   Stream<Path> loadAll();
}