package com.brix.Seller_Sync.lwa.service;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.lwa.exception.LWAException;
import com.brix.Seller_Sync.lwa.payload.LWAResponse;

public interface LWAService {

    LWAResponse getAccessToken(Client client) throws LWAException;

}
