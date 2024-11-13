package com.brix.Seller_Sync.product.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.product.Product;
import com.brix.Seller_Sync.product.ProductRepository;
import com.brix.Seller_Sync.product.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product upsertProduct(Product product) {
        Optional<Product> existingProduct = productRepository.findById(product.getId());
        if (existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setSellerSKU(product.getSellerSKU());
            updatedProduct.setAsin(product.getAsin());
            updatedProduct.setStatus(product.getStatus());
            return productRepository.save(updatedProduct);
        } else {
            return productRepository.save(product);
        }
    }

}
