package com.brix.Seller_Sync.store;

import com.brix.Seller_Sync.brand.Brand;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "store")
public class Store extends BaseEntity {

    @Column()
    private String name;

    @Column()
    private Boolean active = true;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Client> clients = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private  List<Brand> brands = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}
