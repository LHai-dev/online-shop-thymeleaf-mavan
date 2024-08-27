package com.onlineshopthymeleaf.controller;

import com.onlineshopthymeleaf.model.Color;
import com.onlineshopthymeleaf.model.Product;
import com.onlineshopthymeleaf.model.Size;
import com.onlineshopthymeleaf.service.CategoryService;
import com.onlineshopthymeleaf.service.ColorService;
import com.onlineshopthymeleaf.service.FilesStorageService;
import com.onlineshopthymeleaf.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ColorService colorService;
    private final FilesStorageService filesStorageService;
    private static final int PAGE_SIZE = 2; // Number of items per page

    @GetMapping("/admin/products")
    public String listProducts(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               Model model) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Product> products;
        if (name.isEmpty()) {
            products = productService.findAll(pageable);
        } else {
            products = productService.searchProducts(name, pageable);
        }
        model.addAttribute("products", products);
        model.addAttribute("searchTerm", name); // For retaining the search term in the input box
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("totalItems", products.getTotalElements());
        return "products/list";
    }


    @GetMapping("/admin/products/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("availableColors", colorService.getAllColor());
        model.addAttribute("availableSizes", Size.values());  // For enum Size
        return "products/form";
    }

    @PostMapping("/admin/products/save")
    public String saveProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "files", required = false) MultipartFile[] files,
                              @RequestParam(value = "existingImages", required = false) List<String> existingImages,
                              @RequestParam(value = "sizes", required = false) List<Size> selectedSizes,
                              Model model) {

        List<String> imageNames = new ArrayList<>();

        // Validate selected sizes
        if (selectedSizes == null || selectedSizes.isEmpty()) {
            model.addAttribute("message", "Please select at least one size.");
            return "redirect:/admin/products/create";  // Return to the form with an error message
        }

        // Add existing images to the list
        if (existingImages != null) {
            imageNames.addAll(existingImages);
        }

        // Process the uploaded files
        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            Arrays.stream(files).forEach(file -> {
                try {
                    String fileName = filesStorageService.save(file);  // Save the file and get its name
                    imageNames.add("/files/"+fileName);  // Add file name to the list
                } catch (Exception e) {
                    model.addAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
                    return;
                }
            });
        }

        // Set the sizes to the product
        product.setSizes(selectedSizes);

        // Set the images to the product
        product.setImages(imageNames);

        // Save the product with images and sizes
        productService.save(product);

        return "redirect:/admin/products";
    }


    @GetMapping("/admin/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("availableColors", colorService.getAllColor());
        model.addAttribute("availableSizes", Size.values());  // For enum Size

        return "products/form";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Error occurred while trying to delete the product.");
        }
        return "redirect:/admin/products";
    }


    @GetMapping("/products/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        List<String> images = productService.imagesProduct(id); // Assume this service returns the list of image filenames for the product

        model.addAttribute("product", product);
        model.addAttribute("images", images);
        log.info("images {}",images);
        // Return the name of the Thymeleaf template
        return "products/product-details";
    }


    @PostMapping("/admin/products/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @ModelAttribute("product") Product product,
                                @RequestParam(value = "files", required = false) MultipartFile[] files,
                                @RequestParam(value = "sizes", required = false) List<Size> selectedSizes,
                                @RequestParam(value = "colors", required = false) List<Long> selectedColorIds,
                                Model model) {
        Product existingProduct = productService.findById(id);
        if (existingProduct == null) {
            return "redirect:/admin/products";
        }

        List<String> imageNames = new ArrayList<>();

        if (existingProduct.getImages() != null) {
            imageNames.addAll(existingProduct.getImages());
        }

        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            Arrays.stream(files).forEach(file -> {
                try {
                    String fileName = filesStorageService.save(file);
                    imageNames.add(fileName);
                } catch (Exception e) {
                    model.addAttribute("message", "Failed to upload file: " + file.getOriginalFilename());
                }
            });
        }

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImages(imageNames);

        if (selectedSizes != null && !selectedSizes.isEmpty()) {
            existingProduct.setSizes(selectedSizes);
        }

        if (selectedColorIds != null && !selectedColorIds.isEmpty()) {
            List<Color> selectedColors = colorService.findAllById(selectedColorIds);
            existingProduct.setColors(selectedColors);
        }

        productService.save(existingProduct);

        return "redirect:/admin/products";
    }


}
