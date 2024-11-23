package com.brix.Seller_Sync.listing;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.brix.Seller_Sync.listing.payload.CreateListingRequest;
import com.brix.Seller_Sync.listing.service.ListingService;
import com.brix.Seller_Sync.marketplace.Marketplace;
import com.brix.Seller_Sync.reportqueue.ReportQueue;
import com.brix.Seller_Sync.reportqueue.ReportQueueService;

import lombok.extern.java.Log;

@Component
@Log
public class ListingScheduledTasks {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AmznSPReportService amznSPReportService;

    @Autowired
    private ListingService listingService;

    @Autowired
    private ReportQueueService reportQueueService;


    // @Scheduled(cron = "*/30 * * * * ?") // Every 30 seconds
    @Scheduled(cron = "0 0 0 * * ?") // This cron expression means every day at midnight
    public void createListingReport() {
        // TODO move this to a service with a filter for all the store clients
        List<Client> clients = clientService.getAllSPClientsToken();

        for (Client client : clients){
            try {
                List<Marketplace> marketplaces = client.getStore().getMarketplaces();

                List<String> marketplaceIds = marketplaces.stream().map(Marketplace::getMarketplaceId).collect(Collectors.toList());

                CreateReportSpecification createReportSpecification = new CreateReportSpecification();
                createReportSpecification.setReportType(ReportType.GET_MERCHANT_LISTINGS_ALL_DATA);
                createReportSpecification.setMarketplaceIds(marketplaceIds);


                ReportQueue reportQueue = new ReportQueue(client, createReportSpecification);

                if (!reportQueueService.isReportInQueue(reportQueue)){
                    CreateReportResponse createReportResponse = amznSPReportService.createReport(client, createReportSpecification);

                    reportQueueService.enqueueReport(reportQueue, createReportResponse.getReportId());
                }
            } catch (Exception e) {
                log.severe("Failed to create report for client: " + client.getClientId() + " due to: " + e.getMessage());
            }
        }
    }

    @Scheduled(fixedDelay = 5000) // This cron expression means every 5 seconds
    public void getAllReports(){
        ConcurrentHashMap<String, String> reportQueue = reportQueueService.getQueuedReports(ReportType.GET_MERCHANT_LISTINGS_ALL_DATA);

        if (!reportQueue.isEmpty()){
            for (String key : reportQueue.keySet()){
                try {

                    String clientId = reportQueueService.getClientIdFromKey(key);
                    Client client = clientService.getClientByClientId(clientId);
                    String reportId = reportQueue.get(key);

                    CreateReportResponse createReportResponse = new CreateReportResponse(reportId);

                    Report report = amznSPReportService.getReport(client, createReportResponse);

                    if (report.getReportDocumentId() != null){
                        ReportDocument reportDocument = amznSPReportService.getReportDocument(client, report);
                        List<CreateListingRequest> createListingRequests = listingService.parseListingDocument(reportDocument);

                        log.info("Update listings for client: " + client.getClientId());
                        for (CreateListingRequest createListingRequest : createListingRequests){
                            createListingRequest.setStoreId(client.getStore().getId());
                            listingService.upsertListing(createListingRequest);
                        }
                        
                    }
                } catch (Exception e) {
                    log.info("Failed to get report for key: " + key + " due to: " + e.getMessage());
                } finally {
                    reportQueueService.dequeueReport(key);
                }
            }
        }
    }
}