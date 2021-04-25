package com.searchlight.models;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

  private List<String> productNames;
  private Double lat; //User lat and longitude
  private Double longi;

}
