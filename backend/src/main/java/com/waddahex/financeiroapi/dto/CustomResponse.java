package com.waddahex.financeiroapi.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse <T> {
  private String message;
  private String timestamp = LocalDateTime.now().toString();
  private Map<String, String> errors;
  private T data;
}