package com.brix.Seller_Sync.amzn.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Money {
    private double amount;
    private String currencyCode;
}
