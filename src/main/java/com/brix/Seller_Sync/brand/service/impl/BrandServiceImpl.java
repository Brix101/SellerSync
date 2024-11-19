package com.brix.Seller_Sync.brand.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.brand.Brand;
import com.brix.Seller_Sync.brand.BrandRepository;
import com.brix.Seller_Sync.brand.service.BrandService;
import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.common.exception.ResourceNotFoundException;
import com.brix.Seller_Sync.common.payload.ApiResponse;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.common.utils.AppUtils;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public PagedResponse<Brand> getAllBrand(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

        Page<Brand> brands = brandRepository.findAll(pageable);

        List<Brand> content = brands.getNumberOfElements() == 0 ? Collections.emptyList() : brands.getContent();

        return new PagedResponse<>(content, brands.getNumber(), brands.getSize(), brands.getTotalElements(), brands.getTotalPages(), brands.isLast());
    }

    @Override
    public ResponseEntity<Brand> getBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand", AppConstants.ID, id));
        return new ResponseEntity<>(brand, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Brand> addBrand(Brand brand) {
        Brand newBrand = brandRepository.save(brand);
        return new ResponseEntity<>(newBrand, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Brand> updateBrand(Long id, Brand newBrand) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand", AppConstants.ID, id));

        brand.setName(newBrand.getName());

        Brand updateBrand = brandRepository.save(brand);

        return new ResponseEntity<>(updateBrand, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteBrand(Long id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Brand", AppConstants.ID, id));
        brandRepository.delete(brand);

        return new ResponseEntity<>(new ApiResponse(true, "Brand deleted successfully"), HttpStatus.OK);
    }

}
