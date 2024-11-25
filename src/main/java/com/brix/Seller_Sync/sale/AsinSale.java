package com.brix.Seller_Sync.sale;

import com.brix.Seller_Sync.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
// @Table(name = "AsinSale")
@Entity
public class AsinSale extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    private String parentAsin;
    private String childAsin;
    private String sku;

    private Integer unitsOrdered;
    private Double orderedProductSalesAmount;
    private String currencyCode;
    private Integer totalOrderItems;
}
