package com.brix.Seller_Sync.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.product.Listing;

@Service
public class ListingService {

    public List<Listing> parseListingDocument(ReportDocument reportDocument) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(reportDocument.getUrl(), HttpMethod.GET, request, String.class);

            List<Listing> listings = new ArrayList<>();

            String data = response.getBody();
            if (data != null) {
                String[] lines = data.split("\n");
                for (int i = 1; i < lines.length; i++) {
                    String line = lines[i].trim();
                    if (!line.isEmpty()) {
                        String[] columns = line.split("\t");
                        String sellerSku = columns[3];
                        String asin = columns[16];
                        String status = columns[28];

                        if (!asin.isEmpty()) {
                            listings.add(new Listing(sellerSku, asin, status));
                        }
                    }
                }
            }
            return listings;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
