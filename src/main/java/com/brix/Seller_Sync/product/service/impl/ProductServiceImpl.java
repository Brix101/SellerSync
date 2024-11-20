package com.brix.Seller_Sync.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.product.Listing;
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
    public Product upsertListing(Listing listing) {
        return productRepository.upsertByAsin(listing.getSellerSku(), listing.getAsin(), listing.getStatus(), listing.getStoreId());
    }

}
