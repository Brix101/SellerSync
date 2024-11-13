package com.brix.Seller_Sync.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findBySellerSKU(String sellerSKU);

    Product findByAsin(String asin);

}
