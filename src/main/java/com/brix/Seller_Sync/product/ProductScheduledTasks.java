
package com.brix.Seller_Sync.product;

import java.util.List;
import java.util.Set;
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
import com.brix.Seller_Sync.common.AppConstants;
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

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression means every day at midnight
    public void createListingReport() {
        log.info("Product cron task executed");
        
        List<Client> clients = clientService.getAllSPClientsToken();

        for (Client client : clients){
            List<Marketplace> marketplaces = client.getStore().getMarketplaces();

            List<String> marketplaceIds = marketplaces.stream().map(Marketplace::getMarketplaceId).collect(Collectors.toList());

            CreateReportSpecification createReportSpecification = new CreateReportSpecification();
            createReportSpecification.setReportType(ReportType.GET_MERCHANT_LISTINGS_ALL_DATA);
            createReportSpecification.setMarketplaceIds(marketplaceIds);

            amznSPReportService.createReport(client, createReportSpecification);
        }
    }

    @Scheduled(fixedDelay = 5000) // This cron expression means every 5 seconds
    public void getAllReports(){
        Set<String> keys = redisTemplate.keys(AppConstants.PREFIX_REPORT_KEY + "*");

        if (keys != null){
            log.info("Parsing reports");
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
                        productService.upsertListing(listing);
                    }

                    redisTemplate.delete(key);
                }
            }
        }

    }
}