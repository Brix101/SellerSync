package com.brix.Seller_Sync.client;

import com.brix.Seller_Sync.common.entity.BaseEntity;
import com.brix.Seller_Sync.lwa.exception.LWAExceptionErrorCode;
import com.brix.Seller_Sync.lwa.payload.LWAAccessTokenRequestMeta;
import com.brix.Seller_Sync.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "clientId") })
public class Client extends BaseEntity {


    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientProvider provider;

    @NotNull
    @Column(nullable = false, unique = true)
    private String clientId;

    @NotNull
    @Column(nullable = false)
    private String clientSecret;

    @NotNull
    @Column(nullable = false)
    private String grantType;
    
    @NotNull
    @Column(nullable = false, length = 1024, columnDefinition = "TEXT")
    private String refreshToken;

    @Column()
    @Enumerated(EnumType.STRING)
    private LWAExceptionErrorCode error;

    @Column(length = 1024, columnDefinition = "TEXT")
    private String errorDescription;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @ToString.Exclude
    private Store store;

    @JsonIgnore
    public LWAAccessTokenRequestMeta toAuthRequest() {
        return new LWAAccessTokenRequestMeta(grantType, clientId, clientSecret, refreshToken);
    }
}
