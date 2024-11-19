package com.brix.Seller_Sync.amzn.payload.marketplace;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMarketplaceParticipationsResponse {
    
    private List<MarketplaceParticipation> payload;


    public List<AmznMarketplace> getMarketplaces() {
        
        List<AmznMarketplace> marketplaces = new ArrayList<>();
        
        for (MarketplaceParticipation mp : payload) {
            marketplaces.add(mp.getMarketplace());
        }
        
        return marketplaces;
    }
    
}
