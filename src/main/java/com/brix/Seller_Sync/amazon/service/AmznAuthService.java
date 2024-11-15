package com.brix.Seller_Sync.amazon.service;

import com.brix.Seller_Sync.amazon.exception.LWAException;
import com.brix.Seller_Sync.amazon.payload.AuthResponse;
import com.brix.Seller_Sync.client.Client;

public interface AmznAuthService {

    AuthResponse getAccessToken(Client client) throws LWAException;

}
