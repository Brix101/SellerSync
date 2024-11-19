package com.brix.Seller_Sync.amzn.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amzn.payload.marketplace.AmznMarketplace;
import com.brix.Seller_Sync.amzn.payload.marketplace.GetMarketplaceParticipationsResponse;
import com.brix.Seller_Sync.amzn.payload.marketplace.MarketplaceEntry;
import com.brix.Seller_Sync.amzn.service.AmznMarketplaceService;
import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.AppConstants;

import lombok.extern.java.Log;

@Service
@Log
public class AmznMarketplaceServiceImpl implements AmznMarketplaceService {

    private List<MarketplaceEntry> marketplaceEntries = Arrays.asList(
            new MarketplaceEntry("A2EUQ1WTGCTBG2", "CA", "Canada", "NorthAmerica"),
            new MarketplaceEntry("ATVPDKIKX0DER", "US", "United States of America", "NorthAmerica"),
            new MarketplaceEntry("A1AM78C64UM0Y8", "MX", "Mexico", "NorthAmerica"),
            new MarketplaceEntry("A2Q3Y263D00KWC", "BR", "Brazil", "NorthAmerica"),
            new MarketplaceEntry("A1RKKUPIHCS9HS", "ES", "Spain", "Europe"),
            new MarketplaceEntry("A1F83G8C2ARO7P", "UK", "United Kingdom", "Europe"),
            new MarketplaceEntry("A13V1IB3VIYZZH", "FR", "France", "Europe"),
            new MarketplaceEntry("AMEN7PMS3EDWL", "BE", "Belgium", "Europe"),
            new MarketplaceEntry("A1805IZSGTT6HS", "NL", "Netherlands", "Europe"),
            new MarketplaceEntry("A1PA6795UKMFR9", "DE", "Germany", "Europe"),
            new MarketplaceEntry("APJ6JRA9NG5V4", "IT", "Italy", "Europe"),
            new MarketplaceEntry("A2NODRKZP88ZB9", "SE", "Sweden", "Europe"),
            new MarketplaceEntry("AE08WJ6YKNBMC", "ZA", "South Africa", "Europe"),
            new MarketplaceEntry("A1C3SOZRARQ6R3", "PL", "Poland", "Europe"),
            new MarketplaceEntry("ARBP9OOSHTCHU", "EG", "Egypt", "Europe"),
            new MarketplaceEntry("A33AVAJ2PDY3EV", "TR", "Turkey", "Europe"),
            new MarketplaceEntry("A17E79C6D8DWNP", "SA", "Saudi Arabia", "Europe"),
            new MarketplaceEntry("A2VIGQ35RCS4UG", "AE", "United Arab Emirates", "Europe"),
            new MarketplaceEntry("A21TJRUUN4KGV", "IN", "India", "Europe"),
            new MarketplaceEntry("A19VAU5U5O7RUS", "SG", "Singapore", "FarEast"),
            new MarketplaceEntry("A39IBJ37TRP1C6", "AU", "Australia", "FarEast"),
            new MarketplaceEntry("A1VC38T7YXB528", "JP", "Japan", "FarEast")
    );

    @Override
    public List<AmznMarketplace> getMarketplaceParticipations(Client client) {
        try {
            String url = AppConstants.SP_API_URL + "/sellers/v1/marketplaceParticipations";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-amz-access-token", client.getAccessToken());

            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<GetMarketplaceParticipationsResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                GetMarketplaceParticipationsResponse.class
            );

            List<AmznMarketplace> marketplaces = response.getBody().getMarketplaces();

            List<String> marketplaceIds = marketplaceEntries.stream()
            .map(MarketplaceEntry::getMarketplaceId)
            .collect(Collectors.toList());

            return marketplaces.stream()
            .filter(marketplace -> marketplaceIds.contains(marketplace.getId()))
            .map(marketplace -> {
                MarketplaceEntry entry = marketplaceEntries.stream()
                .filter(marketplaceEntry -> marketplaceEntry.getMarketplaceId().equals(marketplace.getId()))
                .findFirst()
                .orElse(null);

                if (entry != null) {
                    marketplace.setName(entry.getCountry());
                }

                return marketplace;
            })
            .collect(Collectors.toList());
        } catch (Exception e) {
            log.severe("Error getting marketplace participations: " + e.getMessage());
            return Collections.emptyList();
        }

    }

}
