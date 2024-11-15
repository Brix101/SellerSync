package com.brix.Seller_Sync.client.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.payload.PagedResponse;

public interface ClientService {

    PagedResponse<Client> getAllClientByStore(Long storeId, int page, int size);

    List<Client> getAllClient(); 

    ResponseEntity<Client> getClient(Long id);

    ResponseEntity<Client> addClient(Client client);
}
