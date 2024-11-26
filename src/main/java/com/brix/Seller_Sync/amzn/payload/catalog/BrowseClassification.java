package com.brix.Seller_Sync.amzn.payload.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrowseClassification {
    private String displayName;
    private String classificationId;
}