package com.brix.Seller_Sync.amzn.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amzn.payload.CreateReportResponse;
import com.brix.Seller_Sync.amzn.payload.CreateReportSpecification;
import com.brix.Seller_Sync.amzn.payload.Report;
import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.service.AmznSPReportService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.AppConstants;

import lombok.extern.java.Log;

@Service
@Log
public class AmznSPReportServiceImpl implements AmznSPReportService {

    @Override
    public CreateReportResponse createReport(Client client, CreateReportSpecification createReportSpecification) {
        // TODO add a key value pair to hold if there is existing report that is not yet completed

        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/reports";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("x-amz-access-token", client.getAccessToken());
        
        HttpEntity<CreateReportSpecification> request = new HttpEntity<>(createReportSpecification, headers);
        
        ResponseEntity<CreateReportResponse> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            CreateReportResponse.class
        );
        
        return response.getBody();
    }

    @Override
    public Report getReport(Client client, String reportId) {
        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/reports/" + reportId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-amz-access-token", client.getAccessToken());
        
        HttpEntity<?> request = new HttpEntity<>(headers);
        
        ResponseEntity<Report> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
            Report.class
        );
        
        return response.getBody();
    }


    @Override
    public ReportDocument getReportDocument(Client client, String reportDocumentId) {
        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/documents/" + reportDocumentId;
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-amz-access-token", client.getAccessToken());
        
        HttpEntity<?> request = new HttpEntity<>(headers);
        
        ResponseEntity<ReportDocument> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
            ReportDocument.class
        );
        
        log.info("ReportDocument: " + response.getBody().getReportDocumentId());
        return response.getBody();
    }

}