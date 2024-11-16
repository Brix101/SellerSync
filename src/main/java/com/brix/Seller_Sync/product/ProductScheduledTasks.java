
package com.brix.Seller_Sync.product;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.java.Log;

@Component
@Log
public class ProductScheduledTasks {

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression means every day at midnight
    public void performProductCron() {
        // Your product cron task logic here
        log.info("Product cron task executed");
    }

    @Scheduled(cron = "0 0 0 * * ?") // This cron expression means every 30 seconds
    public void performTask() {
        // Your task logic here
        // log.info("Product cron task executed every 30 seconds");
    }
}