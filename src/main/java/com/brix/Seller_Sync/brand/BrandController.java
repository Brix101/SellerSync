package com.brix.Seller_Sync.brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brix.Seller_Sync.brand.service.BrandService;
import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.common.payload.PagedResponse;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public PagedResponse<Brand> getAllBrands(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ){
        return brandService.getAllBrand(page,size);
    }
}
