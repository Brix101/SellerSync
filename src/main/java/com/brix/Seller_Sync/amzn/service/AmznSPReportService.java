package com.brix.Seller_Sync.amzn.service;

import com.brix.Seller_Sync.amzn.payload.CreateReportResponse;
import com.brix.Seller_Sync.amzn.payload.Report;
import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.ReportSpecification;
import com.brix.Seller_Sync.client.Client;

public interface AmznSPReportService {

    CreateReportResponse createReport(Client client, ReportSpecification reportSpecification);

    Report getReport(Client client, CreateReportResponse createReportResponse);

    ReportDocument getReportDocument(Client client, Report report);
}
