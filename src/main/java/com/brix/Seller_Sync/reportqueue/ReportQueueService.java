package com.brix.Seller_Sync.reportqueue;

import java.util.concurrent.ConcurrentHashMap;

import com.brix.Seller_Sync.amzn.payload.ReportSpecification.ReportType;

public interface ReportQueueService {

    void enqueueReport(ReportQueue reportQueueObj, String value);

    void dequeueReport(String key);

    ConcurrentHashMap<String, String> getQueuedReports(ReportType reportType);

    Boolean isReportInQueue(ReportQueue reportQueueObj);

    String getClientIdFromKey(String key);

}
