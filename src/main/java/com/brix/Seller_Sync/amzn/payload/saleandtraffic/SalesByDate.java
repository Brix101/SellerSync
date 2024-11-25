package com.brix.Seller_Sync.amzn.payload.saleandtraffic;

import com.brix.Seller_Sync.amzn.payload.Money;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesByDate {
    private Money orderedProductSales;
    private Money orderedProductSalesB2B;
    private Integer unitsOrdered;
    private Integer unitsOrderedB2B;
    private Integer totalOrderItems;
    private Integer totalOrderItemsB2B;
    private Money averageSalesPerOrderItem;
    private Money averageSalesPerOrderItemB2B;
    private Double averageUnitsPerOrderItem;
    private Double averageUnitsPerOrderItemB2B;
    private Money averageSellingPrice;
    private Money averageSellingPriceB2B;
    private Integer unitsRefunded;
    private Double refundRate;
    private Integer claimsGranted;
    private Money claimsAmount;
    private Money shippedProductSales;
    private Integer unitsShipped;
    private Integer ordersShipped;
}
