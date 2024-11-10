package com.brix.Seller_Sync.store;

import com.brix.Seller_Sync.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<Store> addStore(@RequestBody Store store){
        return storeService.addStore(store);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStore(@PathVariable(name = "id") Long id){
        return storeService.getStore(id);
    }
}
