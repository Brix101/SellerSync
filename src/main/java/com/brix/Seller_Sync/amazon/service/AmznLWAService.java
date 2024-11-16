package com.brix.Seller_Sync.amazon.service;

import com.brix.Seller_Sync.amazon.exception.LWAException;
import com.brix.Seller_Sync.amazon.payload.LWAResponse;
import com.brix.Seller_Sync.client.Client;

public interface AmznLWAService {

    LWAResponse getAccessToken(Client client) throws LWAException;

}
