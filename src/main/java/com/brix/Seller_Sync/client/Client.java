package com.brix.Seller_Sync.client;

import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "clientId")})
public class Client extends BaseEntity {

    @NotNull
    @Column(nullable = false, unique = true)
    private String clientId;

    @NotNull
    @Column(nullable = false)
    private String clientSecret;

    @NotNull
    @Column(nullable = false, length = 1024)
    private String refreshToken;

    @NotNull
    @Column(nullable = false)
    private String grantType;

    @NotNull
    @Column(nullable = false)
    private String provider;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
