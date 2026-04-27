package com.infyme.digitalbanking.repository;

import com.infyme.digitalbanking.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByPaidFromOrPaidToOrderByTransactionDateTimeDesc(String paidFrom, String paidTo);
    Optional<TransactionEntity> findTopByOrderByTransactionIdDesc();
}
