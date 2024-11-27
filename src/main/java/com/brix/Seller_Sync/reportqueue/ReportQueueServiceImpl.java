package com.brix.Seller_Sync.reportqueue;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.amzn.payload.ReportSpecification.ReportType;

import lombok.extern.java.Log;

@Service
@Log
public class ReportQueueServiceImpl implements ReportQueueService {
    // TODO move this to redis or some other in-memory database
    private final ConcurrentHashMap<Object, Object> reportQueues = new ConcurrentHashMap<Object,Object>();

    @Override
    public void enqueueReport(Object queueMeta, Object reportMeta) {
        reportQueues.put(queueMeta, reportMeta);
    }

    @Override
    public void dequeueReport(Object key) {
        if (reportQueues.containsKey(key)){
            reportQueues.remove(key);
        }
    }

    @Override
    public ConcurrentHashMap<Object, Object> getQueuedReports(ReportType reportType) {
        ConcurrentHashMap<Object, Object> chunkReports = new ConcurrentHashMap<Object, Object>();
        int chunkSize = 5;

        if (reportQueues.isEmpty()) {
            return chunkReports;
        }

        int count = 0;
        for (Object queue : reportQueues.keySet()) {
            if (count >= chunkSize) break;

            ReportQueue reportQueueMeta = (ReportQueue) queue;
            Object reportMeta = reportQueues.get(queue);

            if (reportMeta != null && reportQueueMeta.getReportSpecification().getReportType().equals(reportType)) {
                chunkReports.put(queue, reportMeta);
                count++;
            }
        }
        return chunkReports;
    }

    @Override
    public Boolean isReportInQueue(ReportQueue reportQueueMeta) {
        return reportQueues.containsKey(reportQueueMeta);
    }
}