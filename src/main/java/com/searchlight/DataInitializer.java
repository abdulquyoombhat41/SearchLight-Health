package com.searchlight;

import com.searchlight.models.Product;
import com.searchlight.models.Store;
import com.searchlight.models.StoreProduct;
import com.searchlight.repos.ProductRepository;
import com.searchlight.repos.StoreProductRepository;
import com.searchlight.repos.StoreRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

  private StoreRepository storeRepository;
  private ProductRepository productRepository;
  private StoreProductRepository storeProductRepository;

  public DataInitializer(StoreRepository storeRepository, ProductRepository productRepository,
      StoreProductRepository storeProductRepository) {
    this.storeRepository = storeRepository;
    this.productRepository = productRepository;
    this.storeProductRepository = storeProductRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    log.debug("Initializing data...");
    //Store 1 list of products
    List<String> products1 = Arrays.asList("SAAZ 500 MG TAB", "TRIKA 0.5 MG TAB", "LOBAZAM 10MG TAB",
        "WHISPER ULTRA SOFT 2X SOFTER WINGS XL 7'S", "WHISPER CHOICE EXTRA LONG WINGS 6'S",
        "WHISPER ULTRA CLEAN WINGS XL 8S", "STAYFREE SECURE REGULAR 20'S", "STAYFREE SECURE COTTONY WINGS XL 6'S",
        "V WASH PLUS LOTION 100ML", "SOFY BODYFIT REGULAR 7'S", "WHISPER ULTRA NIGHT WINGS XL+ 30'S",
        "SOFY BODYFIT ANTIBACTERIA XL 30'S", "KESH KING HAIR OIL 120 ML");
    Store store1 = storeRepository.save(Store.builder().name("PHARMA")
        .lat(59.426940).longi(24.389970).shipmentCreationTime(LocalTime.parse("10:00"))
        .build());
    for(String p: products1) {
      Product pr = productRepository.save(Product.builder()
          .name(p).build());
      storeProductRepository.save(StoreProduct.builder().product(pr).store(store1).noOfItems(12L).price(384.67).build());
    }

    Store store2 = storeRepository.save(Store.builder().name("FMCG")
        .lat(109.426940).longi(92.389970).shipmentCreationTime(LocalTime.parse("10:00"))
        .build());

    for(int i=0; products1.size()>6 && i<products1.size()-5; i++) {
      Optional<Product> existingProduct = productRepository.findByNameIgnoreCase(products1.get(i));
      storeProductRepository.save(StoreProduct.builder().product(existingProduct.get()).store(store2)
          .noOfItems(10L).price(314.67).build());
    }

  }

}
