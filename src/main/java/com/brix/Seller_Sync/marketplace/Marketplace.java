package com.brix.Seller_Sync.marketplace;

import com.brix.Seller_Sync.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Marketplace extends BaseEntity {

    @Column()
    private String country;

    @Column()
    private String marketplaceId;

    @Column()
    private String countryCode;

    @Column()
    private String defaultCurrencyCode;

    @Column()
    private String defaultLanguageCode;

    @Column()
    private String domain;
}
