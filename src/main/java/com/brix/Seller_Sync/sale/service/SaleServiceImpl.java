package com.brix.Seller_Sync.sale.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.ReportDocument.CompressionAlgorithm;
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficReport;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SaleServiceImpl implements SaleService {

    @Override
    public SalesAndTrafficReport parseReportDocument(ReportDocument reportDocument) {
        String url = reportDocument.getDecodedUrl();
        RestTemplate restTemplate = new RestTemplate();
        byte[] reportData = restTemplate.getForObject(url, byte[].class);

        try (InputStream inputStream = new ByteArrayInputStream(reportData);
             InputStream decompressedStream = getDecompressedStream(inputStream, reportDocument.getCompressionAlgorithm());
             InputStreamReader reader = new InputStreamReader(decompressedStream);
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

    private InputStream getDecompressedStream(InputStream inputStream, CompressionAlgorithm compressionAlgorithm) throws IOException {
        switch (compressionAlgorithm) {
            case GZIP:
                return new GZIPInputStream(inputStream);
            case ZIP:
                return new ZipInputStream(inputStream);
            case NONE:
                return inputStream;
            default:
                throw new IllegalArgumentException("Unsupported compression algorithm: " + compressionAlgorithm);
        }
    }
}
