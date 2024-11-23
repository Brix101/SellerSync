
package com.brix.Seller_Sync.amzn.payload.saleandtraffic;

import com.brix.Seller_Sync.amzn.payload.Money;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesByAsin {
    private int unitsOrdered;
    private int unitsOrderedB2B;
    private Money orderedProductSales;
    private Money orderedProductSalesB2B;
    private int totalOrderItems;
    private int totalOrderItemsB2B;
}