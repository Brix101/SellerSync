package com.brix.Seller_Sync.brand.service;

import org.springframework.http.ResponseEntity;

import com.brix.Seller_Sync.brand.Brand;
import com.brix.Seller_Sync.common.payload.ApiResponse;
import com.brix.Seller_Sync.common.payload.PagedResponse;

public interface BrandService {
    
    PagedResponse<Brand> getAllBrand(int page, int size);

    ResponseEntity<Brand> getBrand(Long id);

    ResponseEntity<Brand> addBrand(Brand brand);

    ResponseEntity<Brand> updateBrand(Long id, Brand newBrand);

    ResponseEntity<ApiResponse> deleteBrand(Long id);
}
