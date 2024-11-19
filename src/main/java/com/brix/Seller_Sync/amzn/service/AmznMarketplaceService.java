package com.brix.Seller_Sync.amzn.service;

import java.util.List;

import com.brix.Seller_Sync.amzn.payload.marketplace.AmznMarketplace;
import com.brix.Seller_Sync.client.Client;

public interface AmznMarketplaceService {

    List<AmznMarketplace> getMarketplaceParticipations(Client client);
}
