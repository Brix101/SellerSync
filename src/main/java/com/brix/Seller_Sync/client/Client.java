package com.brix.Seller_Sync.client;

import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.store.Store;
import jakarta.persistence.*;

@Entity
public class Client extends BaseEntity {

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String grantType;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
