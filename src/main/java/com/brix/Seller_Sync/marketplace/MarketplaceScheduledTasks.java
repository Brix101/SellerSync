package com.brix.Seller_Sync.marketplace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brix.Seller_Sync.amazon.payload.marketplace.AmznMarketplace;
import com.brix.Seller_Sync.amazon.service.AmznMarketplaceService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;

import lombok.extern.java.Log;

@Component
@Log
public class MarketplaceScheduledTasks {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AmznMarketplaceService amznMarketplaceService;


    @Scheduled(cron = "1 * * * * ?") 
    public void performMarkerplaceUpdateCron(){
        log.info("Marketplace cron task executed");

        List<Client> clients = clientService.getAllClientsToken();

        for (Client client : clients){
            List<AmznMarketplace> marketplaces = amznMarketplaceService.getMarketplaceParticipations(client);

            for (AmznMarketplace marketplace : marketplaces){
                log.info("Marketplace: " + marketplace.toString());
            }

        }

    }


}
