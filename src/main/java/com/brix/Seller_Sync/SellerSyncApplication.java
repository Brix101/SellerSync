package com.brix.Seller_Sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SellerSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellerSyncApplication.class, args);
	}

}
