package com.searchlight.rest;

import com.searchlight.Utils;
import com.searchlight.models.OrderDTO;
import com.searchlight.models.Product;
import com.searchlight.models.ProductDTO;
import com.searchlight.models.Store;
import com.searchlight.models.StoreDTO;
import com.searchlight.models.StoreProduct;
import com.searchlight.models.response.ErrorResponse;
import com.searchlight.models.response.StoreResponse;
import com.searchlight.repos.ProductRepository;
import com.searchlight.repos.StoreProductRepository;
import com.searchlight.repos.StoreRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreController {

  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private StoreProductRepository storeProductRepository;

  @GetMapping("/getStores")
  public ResponseEntity getNearByStores(HttpServletRequest request, @RequestBody OrderDTO order) {
    List<Product> products = productRepository.findByNameIgnoreCaseIn(order.getProductNames());
    List<Long> ids = products.stream().map(p -> p.getId()).collect(Collectors.toList());
    List<StoreProduct> storeProducts = storeProductRepository.findByProductIdInAndNoOfItemsGreaterThan(ids, 0L);
    Double latMin = order.getLat() - 10;
    Double latMax = order.getLat() + 10;
    Double longMin = order.getLongi() - 10;
    Double longMax = order.getLongi() + 10;
    List<Long> storeIds = storeProducts.stream().map(s -> s.getStore().getId()).collect(Collectors.toList());
    List<Store> stores = storeRepository.findByIdInAndLatIsBetweenAndLongiBetween(storeIds,latMin, latMax , longMin, longMax);
    if(products.isEmpty() || storeProducts.isEmpty() || stores.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("No store available for given order"));
    }
    List<StoreResponse> response = new ArrayList<>();
    for(Store store: stores) {
      StoreResponse storeResponse = new StoreResponse(store.getName(), store.getLat(),
          store.getLongi(), store.getShipmentCreationTime());
      double distance = Utils.distance(store.getLat(), store.getLongi(), order.getLat(), order.getLongi());
      int timeInMinutes = Utils.calculateTime(distance);
      storeResponse.setDeliveryDateTime(LocalDateTime.of(LocalDate.now(), storeResponse.getShipmentCreationTime()).plusMinutes(timeInMinutes));
      response.add(storeResponse);
    }
    response.sort(new Comparator<StoreResponse>() {
      @Override
      public int compare(StoreResponse o1, StoreResponse o2) {
        return o1.getDeliveryDateTime().compareTo(o2.getDeliveryDateTime());
      }
    });
    return ResponseEntity.ok(response);
  }

  @PostMapping("/add")
  public ResponseEntity add(HttpServletRequest request, @RequestBody StoreDTO storeDTO) {
    Optional<Store> existingStore = storeRepository.findByName(storeDTO.getName());
    if(existingStore.isPresent()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Store already existing"));
    }
    Store store = storeRepository.save(Store.builder().name(storeDTO.getName())
        .longi(storeDTO.getLongi()).lat(storeDTO.getLat())
        .shipmentCreationTime(LocalTime.parse(storeDTO.getShipmentTime())).build());
    for(ProductDTO p: storeDTO.getProducts()) {
      Optional<Product> existingProduct = productRepository.findByNameIgnoreCase(p.getName());
      Product product = existingProduct.orElse(productRepository.save(Product.builder()
          .name(p.getName()).build()));
      storeProductRepository.save(StoreProduct.builder().product(product).store(store)
          .noOfItems(p.getNoOfItems()).price(p.getPrice()).build());
    }
    return ResponseEntity.ok("Store Saved");
  }
}
