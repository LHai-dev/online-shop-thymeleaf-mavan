package com.controller;

import com.example.sa.model.Product;
import com.example.sa.service.CategoryService;
import com.example.sa.service.FilesStorageService;
import com.example.sa.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final FilesStorageService filesStorageService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }


    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "products/form";
    }
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "files", required = false) MultipartFile[] files,
                              @RequestParam(value = "existingImages", required = false) List<String> existingImages,
                              Model model) {

        List<String> imageNames = new ArrayList<>();

        // Add existing images to the list
        if (existingImages != null) {
            imageNames.addAll(existingImages);
        }

        // Process the uploaded files
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            Arrays.stream(files).forEach(file -> {
                try {
                    String fileName = filesStorageService.save(file);  // Save the file and get its name
                    imageNames.add(fileName);  // Add file name to the list
                } catch (Exception e) {
                    model.addAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
                    return;
                }
            });
        }

        // Set the images to the product
        product.setImages(imageNames);

        // Save the product with images
        productService.save(product);

        return "redirect:/products";
    }


    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "products/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error occurred while trying to delete the product.");
        }
        return "redirect:/products";
    }


    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        List<String> images = productService.imagesProduct(id); // Assume this service returns the list of image filenames for the product

        model.addAttribute("product", product);
        model.addAttribute("images", images);
        log.info("images {}",images);
        // Return the name of the Thymeleaf template
        return "products/product-details";
    }


    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute("product") Product product,
                                @RequestParam(value = "files", required = false) MultipartFile[] files,
                                Model model) {
        Product existingProduct = productService.findById(id);
        if (existingProduct == null) {
            return "redirect:/products";
        }

        List<String> imageNames = new ArrayList<>();

        // Add existing images if not null
        if (existingProduct.getImages() != null) {
            imageNames.addAll(existingProduct.getImages());
        }

        // Process new uploaded files
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            Arrays.stream(files).forEach(file -> {
                try {
                    String fileName = filesStorageService.save(file);  // Save the file and get its name
                    imageNames.add(fileName);  // Add file name to the list
                } catch (Exception e) {
                    model.addAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
                }
            });
        }
        // Update the product with the new data
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImages(imageNames);
        // ... update other fields as necessary
        productService.save(existingProduct);

        return "redirect:/products";
    }
}
