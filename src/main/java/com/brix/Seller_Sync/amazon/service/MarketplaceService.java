package com.brix.Seller_Sync.amazon.service;

import java.util.List;

import com.brix.Seller_Sync.amazon.payload.marketplace.AmznMarketplace;
import com.brix.Seller_Sync.amazon.payload.marketplace.MarketplaceEntry;
import com.brix.Seller_Sync.client.Client;

public interface MarketplaceService {

    List<AmznMarketplace> getMarketplaceParticipations(Client client);

    List<MarketplaceEntry> getMarketplaceEntries();    
}
