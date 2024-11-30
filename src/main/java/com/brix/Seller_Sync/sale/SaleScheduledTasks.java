package com.brix.Seller_Sync.sale;

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
import com.brix.Seller_Sync.amzn.payload.saleandtraffic.SalesAndTrafficReport;
import com.brix.Seller_Sync.amzn.service.AmznSPReportService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.marketplace.Marketplace;
import com.brix.Seller_Sync.reportqueue.ReportQueue;
import com.brix.Seller_Sync.sale.service.SaleService;

import lombok.extern.java.Log;

@Component
@Log
public class SaleScheduledTasks {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AmznSPReportService amznSPReportService;

    @Autowired
    private SaleService saleService;

    private final ConcurrentHashMap<Object, Object> reportQueues = new ConcurrentHashMap<>();


    @Scheduled(cron = "5 * * * * ?") // Every 5 minutes
    // @Scheduled(cron = "0 0 0 * * ?")  // Every day at midnight
    private void createSaleAndTrafficReport() {
        // TODO move this to a service with a filter for all the store clients
        List<Client> clients = clientService.getAllSPClientsToken();

        Map<String, String> reportOption = new HashMap<>();
        reportOption.put("dateGranularity", "DAY");
        reportOption.put("asinGranularity", "SKU");

        // Calculate the date range (current date minus 2 days)
        LocalDate now = LocalDate.now();
        LocalDate twoDaysAgo = now.minusDays(3);
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


                    if (!reportQueues.containsKey(reportQueue)){
                        ReportResponse reportResponse = amznSPReportService.createReport(client, reportSpecification);

                        reportQueues.put(reportQueue, reportResponse);
                    }
                }
            } catch (Exception e) {
                log.severe("Failed to "+ ReportType.GET_SALES_AND_TRAFFIC_REPORT +" for client: " + client.getId() + " due to: " + e.getMessage());
            }
        }
    }


    @Scheduled(fixedDelay = 5000) // This cron expression means every 5 seconds
    public void getAllReports(){
        if (!reportQueues.isEmpty()){
            for (Object queue : reportQueues.keySet()){
                Object queueValue = reportQueues.get(queue);
                if (queueValue == null){
                    continue;
                }

                try {
                    ReportQueue reportQueue = (ReportQueue) queue;

                    Client client = reportQueue.getClient();
                    ReportResponse reportResponse = (ReportResponse) queueValue;
                    
                    Report report = amznSPReportService.getReport(client, reportResponse);
                    
                    if (report.getReportDocumentId() != null){
                        ReportDocument reportDocument = amznSPReportService.getReportDocument(client, report);
                        SalesAndTrafficReport salesAndTrafficReport = saleService.parseReportDocument(reportDocument);

                        Sale sale = saleService.saveSalesReport(client, salesAndTrafficReport);

                        log.info("Saved sales report for client: " + client.getClientId() + " with id: " + sale.getId());
                        reportQueues.remove(queue); // Dequeue only on success
                    }
                } catch (Exception e) {
                    log.severe("Failed to get report for key: " + queue + " due to: " + e.getMessage());
                }
            }
        }
    }

}
