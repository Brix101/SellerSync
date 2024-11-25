package com.brix.Seller_Sync.sale;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Sale findByClientIdAndDateAndCurrencyCode(Long clientId, LocalDate date, String currencyCode);

    List<Sale> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Sale> findByClientIdAndDateBetween(Long clientId, LocalDate startDate, LocalDate endDate);
}
