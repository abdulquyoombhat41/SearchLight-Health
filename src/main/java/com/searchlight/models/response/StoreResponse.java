package com.searchlight.models.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreResponse {
  private String name;
  private Double lat;
  private Double longi;
  private LocalTime shipmentCreationTime;
  private LocalDateTime deliveryDateTime;

  public StoreResponse(String name, Double lat, Double longi, LocalTime shipmentCreationTime) {
    this.name = name;
    this.lat = lat;
    this.longi = longi;
    this.shipmentCreationTime = shipmentCreationTime;
  }

}
