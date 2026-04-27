package com.infyme.digitalbanking.repository;

import com.infyme.digitalbanking.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<BankAccountEntity, Long> {
    List<BankAccountEntity> findByMobileNumber(String mobileNumber);
    Optional<BankAccountEntity> findTopByOrderByAccountNumberDesc();
}
