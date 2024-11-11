package com.brix.Seller_Sync.client.service.impl;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.ClientRepository;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.common.payload.PagedResponse;
import com.brix.Seller_Sync.common.utils.AppConstants;
import com.brix.Seller_Sync.common.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public PagedResponse<Client> getAllClientByStore(Long storeId, int page, int size) {
        AppUtils.validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, AppConstants.CREATED_AT);

        Page<Client> clients = clientRepository.findByStoreId(storeId, pageable);

        List<Client> content = clients.getNumberOfElements() == 0 ? Collections.emptyList() : clients.getContent();


        return new PagedResponse<>(content, clients.getNumber(), clients.getSize(), clients.getTotalElements(), clients.getTotalPages(), clients.isLast());
    }

    @Override
    public ResponseEntity<Client> addClient(Client client) {
        Client newClient = clientRepository.save(client);

//        newClient.setStore()

        return new ResponseEntity<>(newClient, HttpStatus.OK);
    }
}
