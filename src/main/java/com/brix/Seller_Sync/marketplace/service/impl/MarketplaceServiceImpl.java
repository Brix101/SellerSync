package com.brix.Seller_Sync.marketplace.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.common.utils.AppUtils;
import com.brix.Seller_Sync.marketplace.Marketplace;
import com.brix.Seller_Sync.marketplace.MarketplaceReposity;
import com.brix.Seller_Sync.marketplace.service.MarketplaceService;

@Service
public class MarketplaceServiceImpl implements MarketplaceService {

    @Autowired
    private MarketplaceReposity marketplaceRepository;

    @Override
    public PagedResponse<Marketplace> getMarterplacesByStore(Long storeId, int page, int size) {

        AppUtils.validatePageNumberAndSize(page, size);
        
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Marketplace> marketplaces = marketplaceRepository.findByStoreId(storeId, pageable);

        List<Marketplace> content = marketplaces.getNumberOfElements() == 0 ? null : marketplaces.getContent();
        
        return new PagedResponse<>(content, marketplaces.getNumber(), marketplaces.getSize(), marketplaces.getTotalElements(), marketplaces.getTotalPages(), marketplaces.isLast());
    }


    // @Override
    // public ResponseEntity<Marketplace> getMarketplace(Long id) {
    //     Marketplace marketplace = marketplaceRepository.findById(id)
    //     .orElseThrow(() -> new ResourceNotFoundException("Marketplace", AppConstants.ID, id));

    //     return new ResponseEntity<>(marketplace, HttpStatus.OK);
    // }

    @Override
    public ResponseEntity<Marketplace> addMarketplace(Marketplace marketplace) {
        Marketplace newMarketplace =  marketplaceRepository.save(marketplace);

        return new ResponseEntity<>(newMarketplace, HttpStatus.CREATED);
    }

    // @Override
    // public ResponseEntity<ApiResponse> deleteMarketplace(Long id) {
    //     Marketplace marketplace = marketplaceRepository.findById(id)
    //     .orElseThrow(() -> new ResourceNotFoundException("Marketplace", AppConstants.ID, id));

    //     marketplaceRepository.delete(marketplace);

    //     return new ResponseEntity<>(new ApiResponse(true, "Marketplace deleted successfully"), HttpStatus.OK);
    // }

    @Override
    public List<Marketplace> bulkInsert(List<Marketplace> marketplaces) {
        return marketplaceRepository.saveAll(marketplaces);
    }

}
