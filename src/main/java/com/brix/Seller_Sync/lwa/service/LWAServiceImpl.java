package com.brix.Seller_Sync.lwa.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.brix.Seller_Sync.client.Client;
import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.lwa.exception.LWAException;
import com.brix.Seller_Sync.lwa.payload.LWAAccessTokenRequestMeta;
import com.brix.Seller_Sync.lwa.payload.LWATokenResponse;

import lombok.extern.java.Log;

@Service
@Log
public class LWAServiceImpl implements LWAService {
   
    @Autowired
    private StringRedisTemplate redisTemplate;


    private LWATokenResponse refreshAccessToken(Client client) throws LWAException {
        String url = "https://api.amazon.com/auth/o2/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<LWAAccessTokenRequestMeta> request = new HttpEntity<>(client.toAuthRequest(), headers);

        try {
            log.info("Refreshing access token for client " + client.getClientId());

            ResponseEntity<LWATokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request, LWATokenResponse.class);
            
            return response.getBody();
            
        }  catch (HttpClientErrorException.BadRequest e) {
            LWATokenResponse responseBody = e.getResponseBodyAs(LWATokenResponse.class);
            
            throw new LWAException(responseBody.getError(), responseBody.getErrorDescription(), "Error getting LWA Token");
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong while refreshing the access token", e);
        }

    }

    private void addAccessToken(String clientId, LWATokenResponse token) {
        redisTemplate.opsForValue().set(AppConstants.PREFIX_CLIENT_KEY + clientId, token.getAccessToken(), token.getExpiresIn() - 60, TimeUnit.SECONDS);
    }

    @Override
    public String getAccessToken(Client client) {
        String accessToken = redisTemplate.opsForValue().get(AppConstants.PREFIX_CLIENT_KEY + client.getClientId());

        if (accessToken == null) {
            try {
                LWATokenResponse newToken = refreshAccessToken(client);

                addAccessToken(client.getClientId(), newToken);

                accessToken = newToken.getAccessToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return accessToken;
    }
}