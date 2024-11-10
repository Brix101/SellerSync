package com.brix.Seller_Sync.brand;

import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.product.Product;
import com.brix.Seller_Sync.store.Store;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Brand extends BaseEntity {

    @Column()
    private String name;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}
