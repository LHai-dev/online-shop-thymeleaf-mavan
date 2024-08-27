package com.onlineshopthymeleaf.service;


import com.onlineshopthymeleaf.model.Product;
import com.onlineshopthymeleaf.repository.ProductRepository;
import com.onlineshopthymeleaf.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FileUtil fileUtil;
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Byte id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(Byte id) {
        Product product = findById(id);
        // Delete associated images from the storage
        if (product.getImages() != null) {
            for (String imageName : product.getImages()) {
                fileUtil.deleteByName(imageName);
            }
            product.setImages(new ArrayList<>()); // Clear the images list if needed (optional)
        }
        // Delete the product from the repository
        productRepository.deleteById(id);
    }

    public List<String> imagesProduct(Byte id) {
        findById(id);
        return productRepository.findImagesByProductId(id);
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContaining(name, pageable);
    }
}

