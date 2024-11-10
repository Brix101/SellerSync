package com.brix.Seller_Sync.store;

import com.brix.Seller_Sync.brand.Brand;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.product.Product;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store")
public class Store extends BaseEntity {

    @Column()
    private String name;

    @Column()
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Client> clients = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private  List<Brand> brands = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}
