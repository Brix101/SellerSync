package com.brix.Seller_Sync.client.service;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import org.springframework.http.ResponseEntity;

public interface ClientService {

    PagedResponse<Client> getAllClientByStore(Long storeId, int page, int size);

    ResponseEntity<Client> addClient(Client client);
}
