package com.onlineshopthymeleaf.repository;

import com.onlineshopthymeleaf.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color,Long> {
}
