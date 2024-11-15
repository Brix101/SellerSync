
package com.brix.Seller_Sync.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brix.Seller_Sync.amazon.payload.TokenResponse;
import com.brix.Seller_Sync.amazon.service.AuthService;
import com.brix.Seller_Sync.client.service.ClientService;

import lombok.extern.java.Log;

@Component
@Log
public class ClientScheduledTasks {
   
    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthService authService;

    @Scheduled(cron = "*/30 * * * * ?") // This cron expression means every 30 seconds
    public void performTask() {

        log.info("Client cron task executed every 30 seconds");
       List<Client> clients = clientService.getAllClient();

        for (Client client : clients) {
            TokenResponse tokenResponse = authService.getAccessToken(client);

            log.info("Token Response: " + tokenResponse.toString());
        }

    }
}