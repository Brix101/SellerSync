package com.brix.Seller_Sync.marketplace.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.brix.Seller_Sync.common.payload.ApiResponse;
import com.brix.Seller_Sync.marketplace.Marketplace;

public interface MarketplaceService {

    ResponseEntity<Marketplace> getMarketplace(Long id);

    ResponseEntity<Marketplace> addMarketplace(Marketplace marketplace);

    ResponseEntity<ApiResponse> deleteMarketplace(Long id);

    List<Marketplace> bulkInsert(List<Marketplace> marketplaces);
}
