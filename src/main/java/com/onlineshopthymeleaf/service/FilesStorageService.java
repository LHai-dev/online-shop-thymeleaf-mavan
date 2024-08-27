package com.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
   void init();

    String save(MultipartFile file) throws IOException;

   Resource load(String filename);

   boolean delete(String filename);
  
   void deleteAll();

   Stream<Path> loadAll();
}