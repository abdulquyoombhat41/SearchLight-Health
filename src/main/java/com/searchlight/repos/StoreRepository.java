package com.searchlight.repos;

import com.searchlight.models.Store;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
  List<Store> findByLatIsBetweenAndLongiBetween(Double latMin, Double latMax, Double longiMin, Double longiMax);
  Optional<Store> findByName(String name);
  List<Store> findByIdInAndLatIsBetweenAndLongiBetween(List<Long> ids,Double latMin, Double latMax, Double longiMin, Double longiMax);


}
