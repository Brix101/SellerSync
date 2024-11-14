
package com.brix.Seller_Sync.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brix.Seller_Sync.client.service.ClientService;

import lombok.extern.java.Log;

@Component
@Log
public class ClientScheduledTasks {
   
    @Autowired
    private ClientService clientService;



    @Scheduled(cron = "*/30 * * * * ?") // This cron expression means every 30 seconds
    public void performTask() {

        log.info("Client cron task executed every 30 seconds");
        ResponseEntity<Client> client = clientService.getClient(4L);


        log.info(client.toString());

    }
}