package com.brix.Seller_Sync.amazon.payload.marketplace;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmznMarketplace {
    private String id;
    private String name;
    private String countryCode;
    private String defaultCurrencyCode;
    private String defaultLanguageCode;
    private String domainName;
}
