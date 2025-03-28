package com.waddahex.financeiroapi.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.waddahex.financeiroapi.exception.UnauthorizedException;
import com.waddahex.financeiroapi.model.Transaction;
import com.waddahex.financeiroapi.security.RequestContext;
import com.waddahex.financeiroapi.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@Validated
public class TransactionController {
  @Autowired
  private TransactionService transactionService;

  @PostMapping
  public ResponseEntity<?> create(@Valid @RequestBody Transaction transaction) {
    if(RequestContext.getUser() == null) {
      throw new UnauthorizedException();
    }

    return transactionService.create(transaction);
  }

  @GetMapping
  public ResponseEntity<?> getByCreatedAt(@RequestParam("createdAt") String createdAt) {
    if(RequestContext.getUser() == null) {
      throw new UnauthorizedException();
    }

    return transactionService.getByCreatedAt(createdAt);
  }

  @GetMapping("/monthly")
  public ResponseEntity<?> getMonthtly(@RequestParam("month") String strMonth, @RequestParam("year") String strYear) {
    if(RequestContext.getUser() == null) {
      throw new UnauthorizedException();
    }

    return transactionService.getMonthly(strMonth, strYear);
  }

  @GetMapping("/grouped-by-year")
  public ResponseEntity<?> getGroupedByYear() {
    if(RequestContext.getUser() == null) {
      throw new UnauthorizedException();
    }

    return transactionService.getGroupedByYear();
  }
}