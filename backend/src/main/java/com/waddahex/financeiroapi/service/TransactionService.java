package com.waddahex.financeiroapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.waddahex.financeiroapi.dto.CustomResponse;
import com.waddahex.financeiroapi.dto.TransactionResponse;
import com.waddahex.financeiroapi.model.Transaction;
import com.waddahex.financeiroapi.model.User;
import com.waddahex.financeiroapi.repository.TransactionRepository;
import com.waddahex.financeiroapi.security.RequestContext;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;

  public ResponseEntity<?> create(Transaction transaction) {
    User user = RequestContext.getUser();
    CustomResponse<TransactionResponse> response = new CustomResponse<>();

    transaction.setUser(user);
    if (transaction.getCreatedAt() == null) {
      transaction.setCreatedAt(LocalDate.now());
    }

    Transaction savedTransaction = transactionRepository.save(transaction);
    if (savedTransaction == null) {
      response.setMessage("Erro inesperado ao tentar criar transação!");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    response.setMessage("Transação criada com sucesso!");
    response.setData(new TransactionResponse(savedTransaction));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  public ResponseEntity<?> getByCreatedAt(String createdAt) {
    User user = RequestContext.getUser();
    CustomResponse<List<TransactionResponse>> response = new CustomResponse<>();
    List<Transaction> transactions = Collections.emptyList();

    if (createdAt != null && !createdAt.isBlank()) {
      var date = LocalDate.parse(createdAt);
      transactions = transactionRepository.findByCreatedAtAndUserId(date, user.getId());
    } else {
      transactions = transactionRepository.findByUserId(user.getId());
    }

    if (transactions.isEmpty()) {
      response.setMessage("Nenhuma transação encontrada");
      return ResponseEntity.ok(response);
    }

    response.setData(transactions.stream().map(TransactionResponse::new).collect(Collectors.toList()));
    return ResponseEntity.ok(response);
  }

  public ResponseEntity<?> getMonthly(String strMonth, String strYear) {
    User user = RequestContext.getUser();
    CustomResponse<List<TransactionResponse>> response = new CustomResponse<>();

    if (strMonth == null || strYear == null) {
      response.setMessage("month e year são obrigatórios");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    int month, year;
    try {
      month = Integer.parseInt(strMonth);
      year = Integer.parseInt(strYear);
    } catch (Exception e) {
      response.setMessage("month e year devem ser números");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    if (month < 1 || month > 12) {
      response.setMessage("month deve ser 1-12");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    var yearMonth = YearMonth.of(year, month);
    List<Transaction> transactions = transactionRepository.findByDateBetweenAndUserId(
        yearMonth.atDay(1), yearMonth.atEndOfMonth(), user.getId());

    if (transactions.isEmpty()) {
      response.setMessage("Nenhuma transação encontrada");
      return ResponseEntity.ok(response);
    }

    response.setData(transactions.stream().map(TransactionResponse::new).collect(Collectors.toList()));
    return ResponseEntity.ok(response);
  }

  public ResponseEntity<?> getGroupedByYear() {
    User user = RequestContext.getUser();
    CustomResponse<Map<Integer, List<Integer>>> response = new CustomResponse<>();
    List<Object[]> results = transactionRepository.findDistinctMonthsAndYearsByUserId(user.getId());

    response.setData(results.stream()
        .collect(Collectors.groupingBy(
            row -> (Integer) row[0],
            Collectors.mapping(row -> (Integer) row[1], Collectors.toList()))));

    return ResponseEntity.ok(response);
  }
}
