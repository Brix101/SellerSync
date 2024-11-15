package com.brix.Seller_Sync.client;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "clientId") })
public class Client extends BaseEntity {

    @NotNull
    @Column(nullable = false, unique = true)
    private String clientId;

    @NotNull
    @Column(nullable = false)
    private String clientSecret;

    @NotNull
    @Column(nullable = false)
    private String provider;

    @NotNull
    @Column(nullable = false)
    private String grantType;
    
    @NotNull
    @Column(nullable = false, length = 1024, columnDefinition = "TEXT")
    private String refreshToken;

    @JsonIgnore
    @Column(length = 1024, columnDefinition = "TEXT")
    private String accessToken;

    @JsonIgnore
    @Column()
    private String tokenType;

    @JsonIgnore
    @Column()
    private LocalDateTime expiresAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @ToString.Exclude
    private Store store;

    public void setExpiresAtFromExpiresIn(long expiresIn) {
        this.expiresAt = LocalDateTime.now().plus(expiresIn, ChronoUnit.SECONDS);
    }

    @JsonIgnore
    public boolean isTokenExpired() {
        LocalDateTime localDateTime = LocalDateTime.now();

        return expiresAt == null || expiresAt.isBefore(localDateTime) || expiresAt.isEqual(localDateTime);
    }

}
