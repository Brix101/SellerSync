package com.brix.Seller_Sync.listing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brix.Seller_Sync.listing.payload.CreateListingRequest;

import jakarta.transaction.Transactional;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    Listing findBySellerSKU(String sellerSKU);

    Listing findByAsin(String asin);

    // @Transactional
    // @Query(value = "INSERT INTO Listing (sellerSKU, asin, status, client_id, store_id, created_at, updated_at) VALUES (:sellerSKU, :asin, :status, :clientId,:storeId, NOW(), NOW()) " +
    //                "ON CONFLICT (asin) DO UPDATE SET sellerSKU = :sellerSKU, status = :status, updated_at = NOW() " +
    //                "RETURNING *", nativeQuery = true)
    // Listing upsertByAsin(String sellerSKU, String asin, String status, Long clientId, Long storeId);

    @Transactional
    @Query(value = "INSERT INTO Listing (sellerSKU, asin, status, client_id, store_id, created_at, updated_at) VALUES (:#{#request.sellerSku}, :#{#request.asin}, :#{#request.status}, :#{#request.clientId}, :#{#request.storeId}, NOW(), NOW()) " +
                   "ON CONFLICT (asin) DO UPDATE SET sellerSKU = :#{#request.sellerSku}, status = :#{#request.status}, updated_at = NOW() " +
                   "RETURNING *", nativeQuery = true)
    Listing upsertByAsin(CreateListingRequest request);
}
