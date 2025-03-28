package com.waddahex.financeiroapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.waddahex.financeiroapi.dto.CustomResponse;
import com.waddahex.financeiroapi.model.Transaction;
import com.waddahex.financeiroapi.model.User;
import com.waddahex.financeiroapi.repository.TransactionRepository;
import com.waddahex.financeiroapi.security.RequestContext;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TransactionServiceTests {

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private RequestContext requestContext;

  @InjectMocks
  private TransactionService transactionService;

  private User mockUser;
  private Transaction mockTransaction;

  @BeforeEach
  void setUp() {
    mockUser = new User();
    mockUser.setId(1L);
    RequestContext.setUser(mockUser);

    mockTransaction = new Transaction();
    mockTransaction.setUser(mockUser);
    mockTransaction.setCreatedAt(LocalDate.now());
  }

  @Test
  void createTransaction_ShouldReturnCreatedResponse() {
    // Arrange
    when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);

    // Act
    ResponseEntity<?> response = transactionService.create(mockTransaction);

    // Assert
    assertEquals(201, response.getStatusCode().value());
    CustomResponse<?> body = (CustomResponse<?>) response.getBody();
    assertNotNull(body.getData());
    assertEquals("Transação criada com sucesso!", body.getMessage());
  }

  @Test
  void createTransaction_ShouldReturnErrorWhenSaveFails() {
    // Arrange
    when(transactionRepository.save(any(Transaction.class))).thenReturn(null);

    // Act
    ResponseEntity<?> response = transactionService.create(mockTransaction);

    // Assert
    assertEquals(500, response.getStatusCode().value());
    CustomResponse<?> body = (CustomResponse<?>) response.getBody();
    assertEquals("Erro inesperado ao tentar criar transação!", body.getMessage());
  }

  @Test
  void getByCreatedAt_ShouldReturnTransactions() {
    // Arrange
    String createdAt = "2023-03-25";
    List<Transaction> transactions = Collections.singletonList(mockTransaction);
    when(transactionRepository.findByCreatedAtAndUserId(any(), eq(mockUser.getId())))
        .thenReturn(transactions);

    // Act
    ResponseEntity<?> response = transactionService.getByCreatedAt(createdAt);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    CustomResponse<?> body = (CustomResponse<?>) response.getBody();
    assertNotNull(body.getData());
  }

  @Test
  void getByCreatedAt_ShouldReturnNoTransactionsFound() {
    // Arrange
    String createdAt = "2023-03-25";
    when(transactionRepository.findByCreatedAtAndUserId(any(), eq(mockUser.getId())))
        .thenReturn(Collections.emptyList());

    // Act
    ResponseEntity<?> response = transactionService.getByCreatedAt(createdAt);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    CustomResponse<?> body = (CustomResponse<?>) response.getBody();
    assertEquals("Nenhuma transação encontrada", body.getMessage());
  }

  @Test
  void getMonthly_ShouldReturnTransactionsForMonth() {
    // Arrange
    String strMonth = "3";
    String strYear = "2023";
    List<Transaction> transactions = Collections.singletonList(mockTransaction);
    when(transactionRepository.findByDateBetweenAndUserId(any(), any(), eq(mockUser.getId())))
        .thenReturn(transactions);

    // Act
    ResponseEntity<?> response = transactionService.getMonthly(strMonth, strYear);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    CustomResponse<?> body = (CustomResponse<?>) response.getBody();
    assertNotNull(body.getData());
  }

  @Test
  void getMonthly_ShouldReturnBadRequestWhenInvalidMonth() {
    // Arrange
    String strMonth = "13";
    String strYear = "2023";

    // Act
    ResponseEntity<?> response = transactionService.getMonthly(strMonth, strYear);

    // Assert
    assertEquals(400, response.getStatusCode().value());
    CustomResponse<?> body = (CustomResponse<?>) response.getBody();
    assertEquals("month deve ser 1-12", body.getMessage());
  }
}