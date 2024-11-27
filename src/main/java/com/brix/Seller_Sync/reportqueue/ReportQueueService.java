package com.brix.Seller_Sync.reportqueue;

import java.util.concurrent.ConcurrentHashMap;

import com.brix.Seller_Sync.amzn.payload.ReportSpecification.ReportType;

public interface ReportQueueService {

    void enqueueReport(Object queueMeta, Object reportMeta);

    void dequeueReport(Object key);

    ConcurrentHashMap<Object, Object> getQueuedReports(ReportType reportType);

    Boolean isReportInQueue(ReportQueue reportQueueObj);
}
