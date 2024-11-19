
package com.brix.Seller_Sync.lwa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.brix.Seller_Sync.lwa.payload.LWAAccessTokenRequestMeta;

@Service
public class LWAAccessTokenService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void cacheAccessToken(String token, LWAAccessTokenRequestMeta meta) {
        // redisTemplate.opsForValue().set("access_token", token, meta.getExpiresIn(), TimeUnit.SECONDS);
    }

    public String getAccessToken() {
        return redisTemplate.opsForValue().get("access_token");
    }
}