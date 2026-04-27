package com.infyme.digitalbanking.repository;

import com.infyme.digitalbanking.entity.DigitalBankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DigitalBankAccountRepository extends JpaRepository<DigitalBankAccountEntity, String> {
    boolean existsByMobileNumberAndAccount_AccountNumber(String mobileNumber, Long accountNumber);
    Optional<DigitalBankAccountEntity> findByMobileNumberAndAccount_AccountNumber(String mobileNumber, Long accountNumber);
    List<DigitalBankAccountEntity> findByMobileNumber(String mobileNumber);
}
