package com.brix.Seller_Sync.marketplace;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.brix.Seller_Sync.amzn.payload.marketplace.AmznMarketplace;
import com.brix.Seller_Sync.amzn.service.AmznMarketplaceService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.marketplace.service.MarketplaceService;

import lombok.extern.java.Log;

@Component
@Log
public class MarketplaceScheduledTasks {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AmznMarketplaceService amznMarketplaceService;

    @Autowired
    private MarketplaceService marketplaceService;


    // @Scheduled(cron = "0 0 0 * * ?") 
    @Scheduled(cron = "0 0/30 * * * ?") // Every 30 minutes
    public void performMarkerplaceUpdateCron(){
        log.info("Starting marketplace update cron job");

        List<Client> clients = clientService.getAllSPClientsToken();

        for (Client client : clients){
            List<AmznMarketplace> marketplaces = amznMarketplaceService.getMarketplaceParticipations(client);
            List<Marketplace> storeMarketplaces = client.getStore().getMarketplaces();

            List<String> storeMarketplaceIds = storeMarketplaces.stream().map(Marketplace::getMarketplaceId).collect(Collectors.toList());
            
            for (AmznMarketplace amznMarketplace : marketplaces){
                if (!storeMarketplaceIds.contains(amznMarketplace.getId())){
                    Marketplace marketplace = amznMarketplace.toMarketplace();
                    marketplace.setStore(client.getStore());

                    marketplaceService.addMarketplace(marketplace);
                    log.info("Added marketplace: " + marketplace.getMarketplaceId() + " to store: " + client.getStore().getName());
                }
            }
        }

        log.info("Marketplace update cron job completed");
    }
}
