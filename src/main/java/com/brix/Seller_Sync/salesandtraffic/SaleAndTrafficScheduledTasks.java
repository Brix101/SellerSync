package com.brix.Seller_Sync.saleandtraffic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brix.Seller_Sync.amzn.payload.CreateReportResponse;
import com.brix.Seller_Sync.amzn.payload.CreateReportSpecification;
import com.brix.Seller_Sync.amzn.payload.CreateReportSpecification.ReportType;
import com.brix.Seller_Sync.amzn.service.AmznSPReportService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.marketplace.Marketplace;

import lombok.extern.java.Log;

@Component
@Log
public class SaleAndTrafficScheduledTasks {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AmznSPReportService amznSPReportService;

    private static final ConcurrentHashMap<String, String> reportQueue = new ConcurrentHashMap<>();

    @Scheduled(cron = "*/30 * * * * ?") // Every 30 seconds
    private void createSaleAndTrafficReport() {
        log.info("Starting sale and traffic report cron job");

        // TODO move this to a service with a filter for all the store clients
        List<Client> clients = clientService.getAllSPClientsToken();

        Map<String, String> reportOption = new HashMap<>();
        reportOption.put("dateGranularity", "DAY");
        reportOption.put("asinGranularity", "SKU");

        // Calculate the date range (current date minus 2 days)
        LocalDate now = LocalDate.now();
        LocalDate twoDaysAgo = now.minusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String dataStartTime = twoDaysAgo.format(formatter);

        for (Client client : clients){
            try {
                List<Marketplace> marketplaces = client.getStore().getMarketplaces();

                for (Marketplace marketplace : marketplaces){
                    CreateReportSpecification createReportSpecification = new CreateReportSpecification();
                    createReportSpecification.setReportType(ReportType.GET_SALES_AND_TRAFFIC_REPORT);
                    createReportSpecification.setReportOptions(reportOption);
                    createReportSpecification.setMarketplaceIds(List.of(marketplace.getMarketplaceId()));
                    createReportSpecification.setDataStartTime(dataStartTime);
                    createReportSpecification.setDataEndTime(dataStartTime);

                    CreateReportResponse createReportResponse = amznSPReportService.createReport(client, createReportSpecification);

                    String reportKey = client.getClientId() + ":" + createReportResponse.hashCode();
                    if (createReportResponse.getReportId() != null && !reportQueue.containsKey(reportKey)){
                        log.info("Enqueueing report: " + createReportResponse.getReportId());
                        enqueueReport(reportKey, createReportResponse.getReportId());
                    }
                }
            } catch (Exception e) {
                log.severe("Failed to create sale and traffic report for client: " + client.getId() + " due to: " + e.getMessage());
            }
        }
    }

    private void enqueueReport(String key, String reportId){
        reportQueue.put(key, reportId);
    }

    private void dequeueReport(String key){
        reportQueue.remove(key);
    }
}
