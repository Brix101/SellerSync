package com.brix.Seller_Sync.amzn.payload.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSummary {
    private String marketplaceId;
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