package com.brix.Seller_Sync.salesandtraffic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brix.Seller_Sync.amzn.payload.Report;
import com.brix.Seller_Sync.amzn.payload.ReportDocument;
import com.brix.Seller_Sync.amzn.payload.ReportResponse;
import com.brix.Seller_Sync.amzn.payload.ReportSpecification;
import com.brix.Seller_Sync.amzn.payload.ReportSpecification.ReportType;
import com.brix.Seller_Sync.amzn.service.AmznSPReportService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.marketplace.Marketplace;
import com.brix.Seller_Sync.reportqueue.ReportQueue;
import com.brix.Seller_Sync.reportqueue.ReportQueueService;
import com.brix.Seller_Sync.salesandtraffic.service.SalesAndTrafficService;

import lombok.extern.java.Log;

@Component
@Log
public class SaleAndTrafficScheduledTasks {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AmznSPReportService amznSPReportService;

    @Autowired
    private ReportQueueService reportQueueService;

    @Autowired
    private SalesAndTrafficService salesAndTrafficService;

    @Scheduled(cron = "*/30 * * * * ?") // Every 30 seconds
    // @Scheduled(cron = "0 0 0 * * ?") // This cron expression means every day at midnight
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

                ReportSpecification reportSpecification = new ReportSpecification();
                reportSpecification.setReportType(ReportType.GET_SALES_AND_TRAFFIC_REPORT);
                reportSpecification.setReportOptions(reportOption);
                reportSpecification.setDataStartTime(dataStartTime);
                reportSpecification.setDataEndTime(dataStartTime);

                for (Marketplace marketplace : marketplaces){
                    reportSpecification.setMarketplaceIds(List.of(marketplace.getMarketplaceId()));

                    ReportQueue reportQueue = new ReportQueue(client, reportSpecification);


                    if (!reportQueueService.isReportInQueue(reportQueue)){
                        ReportResponse reportResponse = amznSPReportService.createReport(client, reportSpecification);

                        reportQueueService.enqueueReport(reportQueue, reportResponse.getReportId());
                    }
                }
            } catch (Exception e) {
                log.severe("Failed to create sale and traffic report for client: " + client.getId() + " due to: " + e.getMessage());
            }
        }
    }


    @Scheduled(fixedDelay = 5000) // This cron expression means every 5 seconds
    public void getAllReports(){
        ConcurrentHashMap<String, String> reportQueue = reportQueueService.getQueuedReports(ReportType.GET_SALES_AND_TRAFFIC_REPORT);

        if (!reportQueue.isEmpty()){
            for (String key : reportQueue.keySet()){
                try {

                    String clientId = reportQueueService.getClientIdFromKey(key);
                    Client client = clientService.getClientByClientId(clientId);
                    String reportId = reportQueue.get(key);

                    ReportResponse reportResponse = new ReportResponse(reportId);

                    Report report = amznSPReportService.getReport(client, reportResponse);

                    if (report.getReportDocumentId() != null){
                        ReportDocument reportDocument = amznSPReportService.getReportDocument(client, report);
                        
                        // SalesAndTrafficReport salesAndTrafficReport = salesAndTrafficService.parseReportDocument(reportDocument);

                        // log.info(salesAndTrafficReport.toString());

                        // TODO add parser for sales and traffic here
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
