package com.waddahex.financeiroapi.dto;

import java.time.LocalDate;

import com.waddahex.financeiroapi.model.Transaction;

import lombok.Data;

@Data
public class TransactionResponse {
  private Long id;
  private String description;
  private LocalDate createdAt;
  private LocalDate date;
  private Double amount;
  private Long userId;

  public TransactionResponse(Transaction transaction)
  {
    this.id = transaction.getId();
    this.description = transaction.getDescription();
    this.createdAt = transaction.getCreatedAt();
    this.date = transaction.getDate();
    this.amount = transaction.getAmount();
    this.userId = transaction.getUser().getId();
  }
}