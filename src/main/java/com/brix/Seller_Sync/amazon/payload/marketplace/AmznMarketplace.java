package com.brix.Seller_Sync.amazon.payload.marketplace;

import com.brix.Seller_Sync.marketplace.Marketplace;

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

    public Marketplace toMarketplace() {
        Marketplace marketplace = new Marketplace();
        marketplace.setCountry(this.name);
        marketplace.setMarketplaceId(this.id);
        marketplace.setCountryCode(this.countryCode);
        marketplace.setDefaultCurrencyCode(this.defaultCurrencyCode);
        marketplace.setDefaultLanguageCode(this.defaultLanguageCode);
        marketplace.setDomain(this.domainName);
        return marketplace;
    }
}
