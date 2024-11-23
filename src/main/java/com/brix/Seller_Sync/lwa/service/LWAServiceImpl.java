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
import com.brix.Seller_Sync.client.service.ClientService;
import com.brix.Seller_Sync.common.AppConstants;
import com.brix.Seller_Sync.lwa.exception.LWAException;
import com.brix.Seller_Sync.lwa.exception.LWAExceptionErrorCode;
import com.brix.Seller_Sync.lwa.payload.LWAAccessTokenRequestMeta;
import com.brix.Seller_Sync.lwa.payload.LWATokenResponse;

import lombok.extern.java.Log;

@Service
@Log
public class LWAServiceImpl implements LWAService {
   
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ClientService clientService;


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
            
        } catch (HttpClientErrorException e) {
            LWATokenResponse responseBody = e.getResponseBodyAs(LWATokenResponse.class);
            if (responseBody != null) {
                String error = responseBody.getError();
                String errorDescription = responseBody.getErrorDescription();
                throw new LWAException(error, errorDescription, "Error getting LWA Token");
            } else {
                if (e instanceof HttpClientErrorException.BadRequest) {
                    throw new LWAException("BadRequest", "No response body", "Error getting LWA Token");
                } else if (e instanceof HttpClientErrorException.Unauthorized) {
                    throw new LWAException("invalid_client", "Client authentication failed");
                }else{
                    throw new LWAException(LWAExceptionErrorCode.other.toString(), "Other LWA Exception","Error getting LWA Token");
                }
            }
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
            } catch (LWAException e) {
                
                LWAExceptionErrorCode errorCode = LWAExceptionErrorCode.valueOf(e.getErrorCode());

                clientService.setErrors(client.getId(), errorCode, e.getErrorMessage());
                log.severe(e.getErrorMessage() + " for client: " + client.getClientId());
            } catch (Exception e) {
                clientService.setErrors(client.getId(), LWAExceptionErrorCode.other, e.getMessage());

                log.severe("Error getting access token for client: " + client.getClientId());
            }
        }
        return accessToken;
    }
}
