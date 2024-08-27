package com.service;

import com.example.sa.model.Product;
import com.example.sa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final FilesStorageService filesStorageService;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(Long id) {
        Product product = findById(id);
        // Delete associated images from the storage
        if (product.getImages() != null) {
            for (String imageName : product.getImages()) {
                filesStorageService.delete(imageName);
            }
            product.setImages(new ArrayList<>()); // Clear the images list if needed (optional)
        }
        // Delete the product from the repository
        productRepository.deleteById(id);
    }

    public List<String> imagesProduct(Long id) {
        findById(id);
        return productRepository.findImagesByProductId(id);
    }
}
