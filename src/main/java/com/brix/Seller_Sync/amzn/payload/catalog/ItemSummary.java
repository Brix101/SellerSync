package com.brix.Seller_Sync.amzn.payload.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemSummary {
    private String marketplaceId;
    private Boolean adultProduct;
    private Boolean autographed;
    private String brand;
    private BrowseClassification browseClassification;
    private String color;
    private String itemClassification;
    private String itemName;
    private String manufacturer;
    private String modelNumber;
    private Integer packageQuantity;
    private String partNumber;
    private String size;
    private String style;
    private String websiteDisplayGroup;
}