package com.brix.Seller_Sync.store.service;

import com.brix.Seller_Sync.common.payload.ApiResponse;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.store.Store;
import org.springframework.http.ResponseEntity;

public interface StoreService {

    PagedResponse<Store> getAllStore(int page, int size);

    ResponseEntity<Store> getStore(Long id);

    ResponseEntity<Store> addStore(Store store);

    ResponseEntity<Store> updateStore(Long id, Store newStore);

    ResponseEntity<ApiResponse> deleteStore(Long id);
}
