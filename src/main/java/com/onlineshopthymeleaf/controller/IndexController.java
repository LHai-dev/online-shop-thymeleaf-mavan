package com.onlineshopthymeleaf.controller;

import com.onlineshopthymeleaf.model.Category;
import com.onlineshopthymeleaf.model.Color;
import com.onlineshopthymeleaf.service.CategoryService;
import com.onlineshopthymeleaf.service.ColorService;
import com.onlineshopthymeleaf.service.ProductService;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class IndexController {
    private final ProductService productService;
    private final ColorService colorService;
    private final CategoryService categoryService;

    @GetMapping
    public String homepage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "Homepage";
    }

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }

    @GetMapping("/admin/create-cc")
    public String showCreateForm(Model model) {
        // Initialize the category and color objects if they are not already in the model
        model.addAttribute("category", new Category());
        model.addAttribute("color", new Color());
        List<Color> colors = colorService.getAllColor();
        List<Category> categories = categoryService.findAll();
         model.addAttribute("colors", colors);
         model.addAttribute("categories", categories);

        // Add any other attributes required by the template
        return "admin/create-cc";
    }
    @GetMapping(value = "/admin/overview")
    public String showOverview(){
        return "admin/overview";
    }

}
