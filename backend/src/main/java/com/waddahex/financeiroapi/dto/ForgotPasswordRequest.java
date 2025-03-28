package com.waddahex.financeiroapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
  @NotEmpty(message = "body.email é obrigatório")
  @Email(message = "body.email inválido")
  private String email;
}