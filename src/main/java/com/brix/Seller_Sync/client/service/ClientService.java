package com.brix.Seller_Sync.client.service;

import org.springframework.http.ResponseEntity;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.payload.PagedResponse;

public interface ClientService {

    PagedResponse<Client> getAllClientByStore(Long storeId, int page, int size);

    ResponseEntity<Client> getClient(Long id);

    ResponseEntity<Client> addClient(Client client);
}
