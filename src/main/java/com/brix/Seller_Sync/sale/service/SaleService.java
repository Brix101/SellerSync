package com.brix.Seller_Sync.sale.service;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficReport;

public interface SaleService {

    SalesAndTrafficReport parseReportDocument(ReportDocument reportDocument);

}
