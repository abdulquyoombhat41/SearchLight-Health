package com.searchlight.repos;

import com.searchlight.models.StoreProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreProductRepository extends JpaRepository<StoreProduct, Long> {
  List<StoreProduct> findByProductIdInAndNoOfItemsGreaterThan(List<Long> productIds, Long value);
  Optional<StoreProduct> findByProductIdAndStoreId(Long productId, Long storeId);

}
