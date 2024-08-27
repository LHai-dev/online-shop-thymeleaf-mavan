package com.onlineshopthymeleaf.init;

import com.onlineshopthymeleaf.model.Category;
import com.onlineshopthymeleaf.model.Color;
import com.onlineshopthymeleaf.repository.CategoryRepository;
import com.onlineshopthymeleaf.repository.ColorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ColorRepository colorRepository, CategoryRepository categoryRepository) {
        return args -> {
            // Initialize Colors
            Color red = new Color((byte) 1, "Red", new ArrayList<>());
            Color blue = new Color((byte) 2, "Blue", new ArrayList<>());
            Color green = new Color((byte) 3, "Green", new ArrayList<>());

            colorRepository.saveAll(Arrays.asList(red, blue, green));

            // Initialize only the Clothing category
            Category clothing = new Category((byte) 1, "Clothing");

            categoryRepository.save(clothing);

            System.out.println("Data initialization completed with Clothing category.");
        };
    }
}
