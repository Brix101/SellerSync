package com.brix.Seller_Sync.brand;

import java.util.ArrayList;
import java.util.List;

import com.brix.Seller_Sync.common.BaseEntity;
import com.brix.Seller_Sync.product.Product;
import com.brix.Seller_Sync.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand extends BaseEntity {

    @Column()
    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}
