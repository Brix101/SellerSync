package com.brix.Seller_Sync.store.service;

import com.brix.Seller_Sync.store.Store;
import org.springframework.http.ResponseEntity;

public interface StoreService {

    ResponseEntity<Store> getStore(Long id);

    ResponseEntity<Store> addStore(Store store);
}
