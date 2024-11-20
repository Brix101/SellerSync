package com.brix.Seller_Sync.product;

import com.brix.Seller_Sync.brand.Brand;
import com.brix.Seller_Sync.common.entity.BaseEntity;
import com.brix.Seller_Sync.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product", uniqueConstraints = {@UniqueConstraint(columnNames = "asin")})
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String sellerSKU;

    @Column(unique = true, nullable = false)
    private String asin;

    @Column()
    private String status ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
