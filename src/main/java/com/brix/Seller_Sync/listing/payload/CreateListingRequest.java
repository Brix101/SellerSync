package com.brix.Seller_Sync.listing.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateListingRequest {
    private String sellerSku;
    private String asin;
    private String status;
    private Long clientId;
    private Long storeId;
}
