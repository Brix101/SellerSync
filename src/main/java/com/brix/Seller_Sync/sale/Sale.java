package com.brix.Seller_Sync.sale;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sale", uniqueConstraints = { @UniqueConstraint(columnNames = { "client_id", "date" }) })
public class Sale extends BaseEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    private LocalDate date;

    private Integer unitsOrdered;
    private Integer totalOrderItems;
    private Integer unitsRefunded;
    private Double refundRate;
    private Integer unitsShipped;
    private Integer ordersShipped;

    private String currencyCode;
    private Double orderedProductSalesAmount;
    private Double averageSalesPerOrderItem;
    private Double shippedProductSales;

    @JsonIgnore
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AsinSale> asinSales = new ArrayList<>();
}
