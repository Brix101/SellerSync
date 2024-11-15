package com.brix.Seller_Sync.store.service.impl;

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

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.common.exception.ResourceNotFoundException;
import com.brix.Seller_Sync.common.payload.ApiResponse;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.common.utils.AppConstants;
import com.brix.Seller_Sync.common.utils.AppUtils;
import com.brix.Seller_Sync.store.Store;
import com.brix.Seller_Sync.store.StoreRepository;
import com.brix.Seller_Sync.store.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired 
    private ClientService  clientService;

    @Override
    public PagedResponse<Store> getAllStore(int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");

        Page<Store> stores = storeRepository.findAll(pageable);

        List<Store> content = stores.getNumberOfElements() == 0 ? Collections.emptyList() : stores.getContent();

        return new PagedResponse<>(content, stores.getNumber(), stores.getSize(), stores.getTotalElements(), stores.getTotalPages(), stores.isLast());
    }

    @Override
    public ResponseEntity<Store> getStore(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Store", AppConstants.ID, id));
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Store> addStore(Store store) {
        Store newStore = storeRepository.save(store);
        return new ResponseEntity<>(newStore, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Store> updateStore(Long id, Store newStore) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store", AppConstants.ID, id));

       store.setName(newStore.getName());

        Store updateStore = storeRepository.save(store);

        return new ResponseEntity<>(updateStore, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse> deleteStore(Long id){
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store", AppConstants.ID, id));

        storeRepository.delete(store);

        return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "You successfully deleted store"), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Client> addClient(Long storeId, Client client) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store", AppConstants.ID, storeId));

        client.setStore(store);

        Client newClient = clientService.addClient(client).getBody();

        return new ResponseEntity<>(newClient, HttpStatus.OK);
    }

    @Override
    public List<Store> getAllStoresToken() {
        List<Store> stores = storeRepository.findAll();

        for (Store store : stores) {
            List<Client> clients = clientService.getAllClientsTokenByStoreID(store.getId());

            store.setClients(clients);
        }

        return stores;
    }
}
