package com.waddahex.financeiroapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.waddahex.financeiroapi.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  List<Transaction> findByUserId(Long userId);
  List<Transaction> findByDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
  List<Transaction> findByCreatedAtAndUserId(LocalDate createdAt, Long userId);

  @Query("SELECT DISTINCT EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date) " +
        "FROM Transaction t WHERE t.user.id = :userId " +
        "ORDER BY EXTRACT(YEAR FROM t.date) DESC, EXTRACT(MONTH FROM t.date)")
  List<Object[]> findDistinctMonthsAndYearsByUserId(@Param("userId") Long userId);
}