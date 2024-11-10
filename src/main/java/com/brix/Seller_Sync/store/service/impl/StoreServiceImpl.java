package com.brix.Seller_Sync.store.service.impl;

import com.brix.Seller_Sync.common.exception.ResourceNotFoundException;
import com.brix.Seller_Sync.store.Store;
import com.brix.Seller_Sync.store.StoreRepository;
import com.brix.Seller_Sync.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public ResponseEntity<Store> getStore(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Store", "id", id));
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Store> addStore(Store store) {
        Store newStore = storeRepository.save(store);
        return new ResponseEntity<>(newStore, HttpStatus.CREATED);
    }
}
