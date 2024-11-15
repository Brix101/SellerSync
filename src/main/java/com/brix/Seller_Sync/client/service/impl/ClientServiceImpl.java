package com.brix.Seller_Sync.client.service.impl;

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

import com.brix.Seller_Sync.amazon.exception.LWAException;
import com.brix.Seller_Sync.amazon.payload.TokenResponse;
import com.brix.Seller_Sync.amazon.service.AmznAuthService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.ClientRepository;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.common.exception.ResourceNotFoundException;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.common.utils.AppConstants;
import com.brix.Seller_Sync.common.utils.AppUtils;

import lombok.extern.java.Log;

@Service
@Log
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired AmznAuthService amznAuthService;



    @Override
    public PagedResponse<Client> getAllClientByStore(Long storeId, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Client> clients = clientRepository.findPageByStoreId(storeId, pageable);

        List<Client> content = clients.getNumberOfElements() == 0 ? Collections.emptyList() : clients.getContent();


        return new PagedResponse<>(content, clients.getNumber(), clients.getSize(), clients.getTotalElements(), clients.getTotalPages(), clients.isLast());
    }

    @Override
    public List<Client> getAllClientsToken() {
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            if (client.isTokenExpired()) {
                try {
                    client = refreshAccessToken(client.getId());
                } catch (LWAException e) {
                    // clients.remove(client);
                    continue; // Skip the client that has an error
                }
            }
        }
        return clients;
    }

    @Override
    public List<Client> getAllClientsTokenByStoreID(Long storeId) {
        List<Client> clients = clientRepository.findAllByStoreId(storeId);

        for (Client client : clients) {
            if (client.isTokenExpired()) {
                try {
                    client = refreshAccessToken(client.getId());
                } catch (LWAException e) {
                    clients.remove(client);
                    continue; // Skip the client that has an error
                }
            }
        }
        return clients;
    }


    @Override
    public ResponseEntity<Client> getClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Client", AppConstants.ID, id));

        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Client> addClient(Client client) {
        Client newClient = clientRepository.save(client);

        return new ResponseEntity<>(newClient, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Client> updateClient(Long id, Client client) {
        Client existingClient = clientRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Client", AppConstants.ID, id));

        existingClient.setClientId(client.getClientId());
        existingClient.setClientSecret(client.getClientSecret());
        existingClient.setProvider(client.getProvider());
        existingClient.setGrantType(client.getGrantType());
        existingClient.setRefreshToken(client.getRefreshToken());

        Client updatedClient = clientRepository.save(existingClient);

        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Client> deleteClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Client", AppConstants.ID, id));

        clientRepository.delete(client);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public Client refreshAccessToken(Long id) throws LWAException {
        Client client = clientRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Client", AppConstants.ID, id));

        try {
            TokenResponse tokenResponse = amznAuthService.getAccessToken(client);

            client.setAccessToken(tokenResponse.getAccessToken());
            client.setTokenType(tokenResponse.getTokenType());
            client.setExpiresAtFromExpiresIn(tokenResponse.getExpiresIn());
            client.setError(null);
            client.setErrorDescription(null);

        } catch (LWAException e) {
            client.setAccessToken(null);
            client.setTokenType(null);
            client.setExpiresAt(null);
            client.setError(e.getErrorCode());
            client.setErrorDescription(e.getErrorMessage());
        }

        clientRepository.save(client);

        return client;
    }

}
