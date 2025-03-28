package com.waddahex.financeiroapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordRequest {
  @NotEmpty(message = "body.token é obrigatório")
  private String token;
  @NotEmpty(message = "body.password é obrigatório")
  private String password;
}
