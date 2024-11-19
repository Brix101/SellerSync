package com.brix.Seller_Sync.store;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.common.payload.ApiResponse;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.marketplace.Marketplace;
import com.brix.Seller_Sync.marketplace.service.MarketplaceService;
import com.brix.Seller_Sync.store.service.StoreService;

import jakarta.validation.Valid;
import lombok.extern.java.Log;


@RestController
@RequestMapping("/api/stores")
@Log
public class StoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MarketplaceService marketplaceService;

    @GetMapping
    public PagedResponse<Store> getAllStores(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ){
        return storeService.getAllStore(page,size);
    }

    @PostMapping
    public ResponseEntity<Store> addStore(@Valid @RequestBody Store store){
        return storeService.addStore(store);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStore(@PathVariable(name = "id") Long id){
        return storeService.getStore(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody Store store
    ){
        return storeService.updateStore(id, store);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStore(@PathVariable(name = "id") Long id){
        return storeService.deleteStore(id);
    }

    @GetMapping("/{id}/clients")
    public ResponseEntity<PagedResponse<Client>> getAllClientsByStore(
        @PathVariable(name = "id") Long id,
        @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ){
        PagedResponse<Client> response = clientService.getClientsByStore(id, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/clients")
    public ResponseEntity<Client> addClient(
            @PathVariable(name = "id") Long storeId,
            @RequestBody Client client
    ){
        return storeService.addClient(storeId, client);
    }

    @GetMapping("/{id}/marketplaces")
    public ResponseEntity<PagedResponse<Marketplace>> getAllMarketplacesByStore(
        @PathVariable(name = "id") Long id,
        @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
        @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ){
        PagedResponse<Marketplace> response = marketplaceService.getMarterplacesByStore(id, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
