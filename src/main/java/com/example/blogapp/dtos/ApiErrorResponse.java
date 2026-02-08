package com.example.blogapp.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {

  private String errorMsg;
  private int status;
  private List<AdditionalError> errors;

  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class AdditionalError {
    private String field;
    private String message;
  }
}
