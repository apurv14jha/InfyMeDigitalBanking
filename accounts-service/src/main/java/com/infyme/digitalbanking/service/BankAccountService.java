package com.infyme.digitalbanking.service;

import com.infyme.digitalbanking.dto.BankAccountDTO;

import java.util.List;

public interface BankAccountService {
    String createAccount(BankAccountDTO accountDTO);
    List<BankAccountDTO> listAccounts(String mobileNumber);
    String linkAccount(String mobileNumber, Long accountNumber, Integer otp);
    String checkBalance(String mobileNumber, Long accountNumber);
}
