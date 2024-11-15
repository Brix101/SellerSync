package com.brix.Seller_Sync.amazon.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amazon.payload.TokenResponse;
import com.brix.Seller_Sync.amazon.service.AmzAuthService;
import com.brix.Seller_Sync.client.Client;

import lombok.extern.java.Log;

@Service
@Log
public class AmzAuthServiceImpl implements AmzAuthService {

    @Override
    public TokenResponse getAccessToken(Client client) {
        String url = "https://api.amazon.com/auth/o2/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", client.getGrantType());
        body.put("client_id", client.getClientId());
        body.put("client_secret", client.getClientSecret());
        body.put("refresh_token", client.getRefreshToken());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            log.info("Refreshing access token for client: " + client.getClientId());

            ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, TokenResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.info("Error getting access token" + e.getMessage());
            throw new RuntimeException("Error getting access token ", e);
        }
    }
}
