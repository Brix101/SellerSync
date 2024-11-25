package com.brix.Seller_Sync.amzn.payload;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSpecification {

    public enum ReportType {
        GET_AMAZON_FULFILLED_SHIPMENTS_DATA_GENERAL,
        GET_MERCHANT_LISTINGS_ALL_DATA,
        GET_SALES_AND_TRAFFIC_REPORT
    }

    @NonNull
    private ReportType reportType; // Change type to enum

    private Map<String, String> reportOptions; // Optional reportOptions

    @NonNull
    private List<String> marketplaceIds; // Required marketplaceIds
    
    private String dataStartTime; // Optional dataStartTime
    private String dataEndTime; // Optional dataEndTime
}