package com.brix.Seller_Sync.client;

import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "clientId")})
public class Client extends BaseEntity {

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String grantType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderType providerType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
