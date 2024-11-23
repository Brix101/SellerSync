package com.brix.Seller_Sync.client.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.lwa.exception.LWAExceptionErrorCode;

public interface ClientService {

    PagedResponse<Client> getClientsByStore(Long storeId, int page, int size);

    List<Client> getAllSPClientsToken(); 

    List<Client> getAllClientsTokenByStoreID(Long storeId);

    ResponseEntity<Client> getClient(Long id);

    Client getClientByClientId(String clientId);

    ResponseEntity<Client> addClient(Client client);

    ResponseEntity<Client> updateClient(Long id, Client client);

    Client setErrors(Long id, LWAExceptionErrorCode error, String errorDescription);

    ResponseEntity<Client> deleteClient(Long id);
}
