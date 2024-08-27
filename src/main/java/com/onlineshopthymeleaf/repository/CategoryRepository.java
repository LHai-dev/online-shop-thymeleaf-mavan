package com.repository;

import com.example.sa.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

// CategoryRepository.java
public interface CategoryRepository extends JpaRepository<Category, Long> {}

