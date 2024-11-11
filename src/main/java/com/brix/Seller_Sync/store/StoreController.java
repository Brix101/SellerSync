package com.brix.Seller_Sync.store;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.common.payload.ApiResponse;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.common.utils.AppConstants;
import com.brix.Seller_Sync.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @Autowired
    private ClientService clientService;

    @GetMapping
    public PagedResponse<Store> getAllStores(
            @RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size
    ){
        return storeService.getAllStore(page,size);
    }


    @PostMapping
    public ResponseEntity<Store> addStore(@RequestBody Store store){
        return storeService.addStore(store);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStore(@PathVariable(name = "id") Long id){
        return storeService.getStore(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(
            @PathVariable(name = "id") Long id,
            @RequestBody Store store
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
        PagedResponse<Client> response = clientService.getAllClientByStore(id, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/clients")
    public ResponseEntity<Client> addClient(
            @PathVariable(name = "id") Long id,
            @RequestBody Client client
    ){
        return clientService.addClient(client);
    }

}
