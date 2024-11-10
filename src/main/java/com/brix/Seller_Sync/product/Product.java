package com.brix.Seller_Sync.product;

import com.brix.Seller_Sync.brand.Brand;
import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.store.Store;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Product extends BaseEntity {

    private String sellerSKU;
    private String asin;
    private String status;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
