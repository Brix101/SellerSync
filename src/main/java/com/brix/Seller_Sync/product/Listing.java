package com.brix.Seller_Sync.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Listing {
    private String sellerSku;
    private String asin;
    private String status;
}
