package com.brix.Seller_Sync.listing.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateListingRequest {
    private String sellerSku;
    private String asin;
    private String status;
    private Long storeId;
}
