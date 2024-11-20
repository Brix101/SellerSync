package com.brix.Seller_Sync.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findBySellerSKU(String sellerSKU);

    Product findByAsin(String asin);

    @Transactional
    @Query(value = "INSERT INTO Product (sellerSKU, asin, status, created_at, updated_at) VALUES (:sellerSKU, :asin, :status, NOW(), NOW()) " +
                   "ON CONFLICT (asin) DO UPDATE SET sellerSKU = :sellerSKU, status = :status, updated_at = NOW() " +
                   "RETURNING *", nativeQuery = true)
    Product upsertByAsin(String sellerSKU, String asin, String status);
}
