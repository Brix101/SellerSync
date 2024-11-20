
package com.brix.Seller_Sync.product;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brix.Seller_Sync.amzn.payload.CreateReportResponse;
import com.brix.Seller_Sync.amzn.payload.CreateReportSpecification;
import com.brix.Seller_Sync.amzn.payload.CreateReportSpecification.ReportType;
import com.brix.Seller_Sync.amzn.payload.Report;
import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.service.AmznSPReportService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.marketplace.Marketplace;
import com.brix.Seller_Sync.product.service.ListingService;
import com.brix.Seller_Sync.product.service.ProductService;

import lombok.extern.java.Log;

@Component
@Log
public class ProductScheduledTasks {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AmznSPReportService amznSPReportService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ListingService listingService;

    @Autowired
    private ProductService productService;

    private static final String LISTING_REPORT_KEY = "listing_report:";

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression means every day at midnight
    public void createListingReport() {
        // TODO move this to a service with a filter for all the store clients
        List<Client> clients = clientService.getAllSPClientsToken();
        Set<String> keys = getReportKeys();

        for (Client client : clients){
            List<Marketplace> marketplaces = client.getStore().getMarketplaces();

            List<String> marketplaceIds = marketplaces.stream().map(Marketplace::getMarketplaceId).collect(Collectors.toList());

            CreateReportSpecification createReportSpecification = new CreateReportSpecification();
            createReportSpecification.setReportType(ReportType.GET_MERCHANT_LISTINGS_ALL_DATA);
            createReportSpecification.setMarketplaceIds(marketplaceIds);

            CreateReportResponse createReportResponse = amznSPReportService.createReport(client, createReportSpecification);

            if (createReportResponse.getReportId() != null && !keys.contains(LISTING_REPORT_KEY + client.getClientId())){
                enqueueReport(client.getClientId(), createReportResponse.getReportId());
            }
        }
    }

    @Scheduled(fixedDelay = 5000) // This cron expression means every 5 seconds
    public void getAllReports(){
        Set<String> keys = getReportKeys();

        if (keys != null){
            for (String key : keys){
                String cliendId = key.split(":")[1];

                Client client = clientService.getClientByClientId(cliendId);
                String reportId = redisTemplate.opsForValue().get(key);

                CreateReportResponse createReportResponse = new CreateReportResponse(reportId);

                Report report = amznSPReportService.getReport(client, createReportResponse);

                if (report.getReportDocumentId() != null){
                    ReportDocument reportDocument = amznSPReportService.getReportDocument(client, report);
                    List<Listing> listings = listingService.parseListingDocument(reportDocument);

                    for (Listing listing : listings){
                        listing.setStoreId(client.getStore().getId());
                        productService.upsertListing(listing);
                    }

                    dequeueReport(cliendId);
                }
            }
        }

    }

    private void enqueueReport(String clientId, String reportId){
        redisTemplate.opsForValue().set(LISTING_REPORT_KEY + clientId, reportId, 1, TimeUnit.DAYS);
    }

    private void dequeueReport(String clientId){
        redisTemplate.delete(LISTING_REPORT_KEY + clientId);
    }

    private Set<String> getReportKeys(){
        return redisTemplate.keys(LISTING_REPORT_KEY + "*");
    }
}