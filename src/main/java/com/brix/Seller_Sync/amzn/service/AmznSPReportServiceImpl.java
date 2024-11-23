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

import com.brix.Seller_Sync.amzn.payload.Report;
import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.ReportResponse;
import com.brix.Seller_Sync.amzn.payload.ReportSpecification;
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
    public ReportResponse createReport(Client client, ReportSpecification reportSpecification) {
        String reportKey = client.getClientId() + ":" + reportSpecification.hashCode();

        String existingReportId = redisTemplate.opsForValue().get(reportKey);
        if (existingReportId != null) {
            log.info(reportSpecification.getReportType() + " Report already exists for client " + client.getClientId());
            return new ReportResponse(existingReportId);
        }

        log.info("Creating " + reportSpecification.getReportType() + " report for client " + client.getClientId());

        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/reports";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        String accessToken = lwaService.getAccessToken(client);

        headers.set("Content-Type", "application/json");
        headers.set("x-amz-access-token", accessToken);
        
        HttpEntity<ReportSpecification> request = new HttpEntity<>(reportSpecification, headers);
        
        ResponseEntity<ReportResponse> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            ReportResponse.class
        );

        ReportResponse reportResponse = response.getBody();

        log.info("Created " + reportSpecification.getReportType() + " report for client " + client.getClientId());
        if (reportResponse != null) {
            redisTemplate.opsForValue().set(reportKey, reportResponse.getReportId(), 3600, TimeUnit.SECONDS);
        }

        return reportResponse;
    }

    @Override
    public Report getReport(Client client, ReportResponse reportResponse) {
        log.info("Getting report for reportId: " + reportResponse.getReportId());

        String url = AppConstants.SP_API_URL + "/reports/2021-06-30/reports/" + reportResponse.getReportId();
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

        Report report = response.getBody();

        log.info("Status for reportId: " + report.getReportId() + " is " + report.getProcessingStatus());
        return report;
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

        ReportDocument reportDocument = response.getBody();
        
        log.info("Report document received for reportId: " + report.getReportId());
        return reportDocument;
    }
}
