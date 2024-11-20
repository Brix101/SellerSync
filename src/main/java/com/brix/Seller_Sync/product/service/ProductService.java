package com.brix.Seller_Sync.product.service;

import com.brix.Seller_Sync.product.Listing;
import com.brix.Seller_Sync.product.Product;

public interface ProductService {
    
    Product addProduct(Product product);

    Product upsertListing(Listing listing);

}
