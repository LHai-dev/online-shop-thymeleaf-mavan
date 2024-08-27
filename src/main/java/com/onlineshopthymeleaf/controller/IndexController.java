package com.onlineshopthymeleaf.controller;

import com.onlineshopthymeleaf.service.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomePageController {
    private final ProductService productService;

    @GetMapping
    public String homepage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "Homepage";
    }

    @GetMapping(value = "/index")
    public String index() {
        return "index";
    }
}
