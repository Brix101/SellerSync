package com.brix.Seller_Sync.amazon.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.amazon.exception.LWAException;
import com.brix.Seller_Sync.amazon.payload.TokenResponse;
import com.brix.Seller_Sync.amazon.service.AmznAuthService;
import com.brix.Seller_Sync.client.Client;

import lombok.extern.java.Log;

@Service
@Log
public class AmznAuthServiceImpl implements AmznAuthService {

    @Override
    public TokenResponse getAccessToken(Client client) throws LWAException {
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
            log.info("Refreshing: " + client.getClientId());

            ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, TokenResponse.class);
            
            return response.getBody();
            
        }  catch (HttpClientErrorException.BadRequest e) {
            TokenResponse responseBody = e.getResponseBodyAs(TokenResponse.class);
            
            throw new LWAException(responseBody.getError(), responseBody.getErrorDescription(), "Error getting LWA Token");
        } catch (Exception e) {
            throw new RuntimeException("Error getting LWA Token", e);
        }

    }
}
