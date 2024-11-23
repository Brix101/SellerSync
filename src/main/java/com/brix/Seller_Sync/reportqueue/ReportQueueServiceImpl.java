
package com.brix.Seller_Sync.reportqueue;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.amzn.payload.ReportSpecification.ReportType;

import lombok.extern.java.Log;

@Service
@Log
public class ReportQueueServiceImpl implements ReportQueueService {
    // TODO move this to redis or some other in-memory database
    private final ConcurrentHashMap<String, String> reportQueue = new ConcurrentHashMap<>();

    @Override
    public void enqueueReport(ReportQueue reportQueueObj, String value) {
        String key = reportQueueObj.getReportKey();

        log.info(key + " : " + value);
        reportQueue.put(key, value);
    }

    @Override
    public void dequeueReport(String key) {
        reportQueue.remove(key);
    }

    @Override
    public ConcurrentHashMap<String, String> getQueuedReports(ReportType reportType) {
        ConcurrentHashMap<String, String> reports = new ConcurrentHashMap<>();
        for (String key : reportQueue.keySet()) {
            if (key.startsWith(reportType.toString())) {
                reports.put(key, reportQueue.get(key));
            }
        }
        return reports;
    }

    @Override
    public Boolean isReportInQueue(ReportQueue reportQueueObj) {
        String key = reportQueueObj.getReportKey();
        return reportQueue.containsKey(key);
    }
   
    @Override
    public String getClientIdFromKey(String key) {
        return key.split(":")[1];
    }
}