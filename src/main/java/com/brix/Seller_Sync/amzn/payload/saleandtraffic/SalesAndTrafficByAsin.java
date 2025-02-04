package com.brix.Seller_Sync.amzn.payload.saleandtraffic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesAndTrafficByAsin {
    private String parentAsin;
    private String childAsin;
    private String sku;
    private SalesByAsin salesByAsin;
    private TrafficByAsin trafficByAsin;
}
