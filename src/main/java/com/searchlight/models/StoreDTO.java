package com.searchlight.models;

import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDTO {

  private String name;
  private List<ProductDTO> products;
  private Double lat; //store lat and longitude
  private Double longi;
  private String shipmentTime;

}
