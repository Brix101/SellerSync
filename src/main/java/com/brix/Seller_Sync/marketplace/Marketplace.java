package com.brix.Seller_Sync.marketplace;

import com.brix.Seller_Sync.common.entity.BaseEntity;
import com.brix.Seller_Sync.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
