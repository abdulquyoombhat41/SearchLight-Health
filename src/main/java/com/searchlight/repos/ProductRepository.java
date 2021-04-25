package com.searchlight.repos;

import com.searchlight.models.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByNameIgnoreCaseIn(List<String> names);
  Optional<Product> findByNameIgnoreCase(String name);

}
