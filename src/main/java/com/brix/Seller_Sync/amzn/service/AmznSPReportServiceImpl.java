package com.brix.Seller_Sync.amzn.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.lwa.service.LWAService;

import lombok.extern.java.Log;

@Service
@Log
public class AmznSPReportServiceImpl implements AmznSPReportService {

    @Autowired
    private LWAService lwaService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public CreateReportResponse createReport(Client client, CreateReportSpecification createReportSpecification) {
        String reportKey = client.getClientId() + ":" + createReportSpecification.hashCode();

        String existingReportId = redisTemplate.opsForValue().get(reportKey);
        if (existingReportId != null) {
            log.info(createReportSpecification.getReportType() + " Report already exists for client " + client.getClientId());
            return new CreateReportResponse(existingReportId);
        }

        log.info("Creating " + createReportSpecification.getReportType() + " report for client " + client.getClientId());

        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/reports";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        String accessToken = lwaService.getAccessToken(client);

        headers.set("Content-Type", "application/json");
        headers.set("x-amz-access-token", accessToken);
        
        HttpEntity<CreateReportSpecification> request = new HttpEntity<>(createReportSpecification, headers);
        
        ResponseEntity<CreateReportResponse> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            CreateReportResponse.class
        );

        CreateReportResponse createReportResponse = response.getBody();

        if (createReportResponse != null) {
            redisTemplate.opsForValue().set(reportKey, createReportResponse.getReportId(), 3600, TimeUnit.SECONDS);
        }

        log.info("Report created for client: " + client.getClientId() + " with reportId: " + createReportResponse.getReportId());
        return createReportResponse;
    }

    @Override
    public Report getReport(Client client, CreateReportResponse createReportResponse) {
        log.info("Getting report for reportId: " + createReportResponse.getReportId());

        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/reports/" + createReportResponse.getReportId();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        String accessToken = lwaService.getAccessToken(client);

        headers.set("x-amz-access-token", accessToken);
        
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
    public ReportDocument getReportDocument(Client client, Report report) {
        log.info("Getting report document for reportId: " + report.getReportId());

        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/documents/" + report.getReportDocumentId();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        String accessToken = lwaService.getAccessToken(client);

        headers.set("x-amz-access-token", accessToken);
        
        HttpEntity<?> request = new HttpEntity<>(headers);
        
        ResponseEntity<ReportDocument> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
            ReportDocument.class
        );
        
        return response.getBody();
    }
}
