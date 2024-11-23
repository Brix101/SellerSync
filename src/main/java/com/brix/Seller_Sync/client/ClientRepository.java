package com.brix.Seller_Sync.client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Page<Client> findPageByStoreId(Long storeId, Pageable pageable);

    List<Client> findAllByStoreId(Long storeId);

    List<Client> findAllByProvider(ClientProvider provider);

    List<Client> findAllByProviderAndErrorIsNull(ClientProvider provider);

    Optional<Client> findByClientId(String clientId);
}
