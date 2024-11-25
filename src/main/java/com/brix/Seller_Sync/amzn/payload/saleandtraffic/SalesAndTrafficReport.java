package com.brix.Seller_Sync.amzn.payload.saleandtraffic;

import java.util.List;

import com.brix.Seller_Sync.amzn.payload.ReportSpecification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesAndTrafficReport {
    private ReportSpecification reportSpecification;
    private List<SalesAndTrafficByDate> salesAndTrafficByDate;
    private List<SalesAndTrafficByAsin> salesAndTrafficByAsin;
}
