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
    private int unitsOrdered;
    private int totalOrderItems;
    private Money averageSalesPerOrderItem;
    private int averageUnitsPerOrderItem;
    private Money averageSellingPrice;
    private int unitsRefunded;
    private double refundRate;
    private int claimsGranted;
    private Money claimsAmount;
    private Money shippedProductSales;
    private int unitsShipped;
    private int ordersShipped;
}
