package com.onlineshopthymeleaf.repository;

import com.onlineshopthymeleaf.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// CategoryRepository.java
public interface CategoryRepository extends JpaRepository<Category, Byte> {}

