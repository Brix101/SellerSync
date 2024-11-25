package com.brix.Seller_Sync.salesandtraffic.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficReport;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SalesAndTrafficServiceImpl implements SalesAndTrafficService {

    @Override
    public SalesAndTrafficReport parseReportDocument(ReportDocument reportDocument) {
        String url = reportDocument.getDecodedUrl();
        RestTemplate restTemplate = new RestTemplate();
        byte[] reportData = restTemplate.getForObject(url, byte[].class);

        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(reportData));
             InputStreamReader reader = new InputStreamReader(gis);
             BufferedReader bufferedReader = new BufferedReader(reader)) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(sb.toString(), SalesAndTrafficReport.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
