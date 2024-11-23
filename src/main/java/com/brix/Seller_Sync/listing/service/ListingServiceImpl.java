package com.brix.Seller_Sync.listing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.listing.Listing;
import com.brix.Seller_Sync.listing.ListingRepository;
import com.brix.Seller_Sync.listing.payload.CreateListingRequest;

import lombok.extern.java.Log;

@Service
@Log
public class ListingServiceImpl implements ListingService {
    
    @Autowired
    private ListingRepository listingRepository;

    @Override
    public Listing addListing(Listing Listing) {
        return listingRepository.save(Listing);
    }

    @Override
    public Listing upsertListing(CreateListingRequest createListingRequest) {
        return listingRepository.upsertByAsin(createListingRequest.getSellerSku(), createListingRequest.getAsin(), createListingRequest.getStatus(), createListingRequest.getStoreId());
    }

    @Override
    public List<CreateListingRequest> parseReportDocument(ReportDocument reportDocument) {
        try {
            String url = reportDocument.getDecodedUrl();

            RestTemplate restTemplate = new RestTemplate();
            String data = restTemplate.getForObject(url, String.class);

            List<CreateListingRequest> createListingRequests = new ArrayList<>();

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
                            createListingRequests.add(new CreateListingRequest(sellerSku, asin, status, 0L));
                        }
                    }
                }
            }
            return createListingRequests;
        } catch (Exception e) {
            log.info(e.getMessage());
            return new ArrayList<>();
        }
    }
}
