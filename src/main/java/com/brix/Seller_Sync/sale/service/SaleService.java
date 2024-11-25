package com.brix.Seller_Sync.sale.service;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficReport;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.sale.Sale;

public interface SaleService {

    SalesAndTrafficReport parseReportDocument(ReportDocument reportDocument);

    Sale saveSalesReport(Client client, SalesAndTrafficReport salesAndTrafficReport);

}
