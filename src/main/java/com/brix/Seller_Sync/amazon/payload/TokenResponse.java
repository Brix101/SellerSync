package com.brix.Seller_Sync.amazon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
}
