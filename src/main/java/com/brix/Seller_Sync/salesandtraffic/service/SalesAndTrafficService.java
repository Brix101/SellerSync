package com.brix.Seller_Sync.salesandtraffic.service;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficReport;

public interface SalesAndTrafficService {

    SalesAndTrafficReport parseReportDocument(ReportDocument reportDocument);

}
