package com.onlineshopthymeleaf.controller;

import com.onlineshopthymeleaf.model.Color;
import com.onlineshopthymeleaf.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public String listColors(Model model) {
        List<Color> colors = colorService.getAllColor();
        model.addAttribute("colors", colors);
        return "redirect:/admin/create-cc";
    }

    @GetMapping("/create")
    public String createColorForm(Model model) {
        model.addAttribute("color", new Color());
        return "redirect:/admin/create-cc";
    }

    @PostMapping("/save")
    public String saveColor(@ModelAttribute("color") Color color) {
        colorService.saveColor(color);
        return "redirect:/admin/create-cc";
    }

    @GetMapping("/edit/{id}")
    public String editColorForm(@PathVariable Long id, Model model) {
        Color color = colorService.getColorById(id);
        model.addAttribute("color", color);
        return "redirect:/admin/create-cc";
    }

    @PostMapping("/update/{id}")
    public String updateColor(@PathVariable Long id, @ModelAttribute("color") Color color) {
        color.setId(id);
        colorService.saveColor(color);
        return "redirect:/admin/create-cc";
    }

    @GetMapping("/delete/{id}")
    public String deleteColor(@PathVariable Long id) {
        colorService.deleteColor(id);
        return "redirect:/admin/colors";
    }
}
