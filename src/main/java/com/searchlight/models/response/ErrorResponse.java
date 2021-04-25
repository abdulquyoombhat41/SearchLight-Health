package com.searchlight.models.response;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

  private int status;
  private String message;

  public ErrorResponse(String message) {
    this.message = message;
  }

  public ErrorResponse(String message, int status) {
    this(message);
    this.status = status;
  }

}
