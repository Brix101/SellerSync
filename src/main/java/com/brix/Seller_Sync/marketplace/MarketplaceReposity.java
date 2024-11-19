package com.brix.Seller_Sync.marketplace;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketplaceReposity extends JpaRepository<Marketplace, Long> {

    Page<Marketplace> findByStoreId(Long storeId, Pageable pageable);

    List<Marketplace> findAllByStoreId(Long storeId);
}
