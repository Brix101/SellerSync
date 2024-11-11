package com.brix.Seller_Sync.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findByStoreId(Long storeId, Pageable pageable);

}
