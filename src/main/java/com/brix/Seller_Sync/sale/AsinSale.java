package com.brix.Seller_Sync.sale;

import com.brix.Seller_Sync.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AsinSale extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sale_id")
    @ToString.Exclude
    private Sale sale;

    @Column()
    private String parentAsin;
    
    @Column()
    private String childAsin;
    
    @Column()
    private String sku;

    @Column()
    private Integer unitsOrdered;

    @Column()
    private Double orderedProductSalesAmount;
    
    @Column()
    private String currencyCode;
    
    @Column()
    private Integer totalOrderItems;
}
