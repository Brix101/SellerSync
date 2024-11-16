package com.brix.Seller_Sync.amazon.service;

import com.brix.Seller_Sync.amazon.payload.CreateReportResponse;
import com.brix.Seller_Sync.amazon.payload.CreateReportSpecification;
import com.brix.Seller_Sync.amazon.payload.Report;
import com.brix.Seller_Sync.amazon.payload.ReportDocument;
import com.brix.Seller_Sync.client.Client;

public interface AmznSPReportService {

    CreateReportResponse createReport(Client client, CreateReportSpecification createReportSpecification);

    Report getReport(Client client, String reportId);

    ReportDocument getReportDocument(Client client, String reportDocumentId);
}
