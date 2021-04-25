package com.searchlight.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchlight.models.Product;
import com.searchlight.models.Store;
import com.searchlight.models.StoreProduct;
import com.searchlight.repos.ProductRepository;
import com.searchlight.repos.StoreProductRepository;
import com.searchlight.repos.StoreRepository;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OfflineServiceSchedular {

  private static String url = "http://localhost:9991/api";

  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private StoreProductRepository storeProductRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ObjectMapper jsonMapper;

  @Autowired
  private HttpClientService httpClientService;


  @Scheduled(fixedDelay = 3600000)
  public void updateNumberOfItems() {
    log.info("Fetching data for offline orders");
    List<Store> stores = storeRepository.findAll();
    for(Store store: stores) {
      ResponseEntity<String> response = httpClientService.fetchResponse(Optional.empty(), HttpMethod.GET,
          url+ String.format("/store/{id}", store.getId()), Optional.empty(), Optional.empty());
      if(response.getStatusCode() == HttpStatus.OK) {
        try {
          String body = response.getBody();
          //based on response we can get a list of products sold
          List<JsonNode> list = jsonMapper.readValue(body, new TypeReference<List<JsonNode>>(){});
          for(JsonNode json: list) {
            String name = json.get("item").toString();
            Long noOfItemsSold = Long.valueOf(json.get("sold").toString());
            Optional<Product> pr = productRepository.findByNameIgnoreCase(name);
            pr.ifPresent(p -> {
              Optional<StoreProduct> storeProduct = storeProductRepository.findByProductIdAndStoreId(p.getId(), store.getId());
              storeProduct.ifPresent(sp -> {
                Long noOfItems = (sp.getNoOfItems() - noOfItemsSold > 0) ? sp.getNoOfItems() - noOfItemsSold : 0L;
                sp.setNoOfItems(noOfItems);
                storeProductRepository.save(sp);
              });
            });
          }

        } catch (Exception e) {
          log.info(String.format("Error in converting Json to response node message= %s", e.getMessage()));
        }
      }
    }

  }

}
