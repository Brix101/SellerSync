package com.brix.Seller_Sync.amazon.payload.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketplaceEntry {
    private String marketplaceId;
    private String country;
    private String countryCode;
    private String region;
}
