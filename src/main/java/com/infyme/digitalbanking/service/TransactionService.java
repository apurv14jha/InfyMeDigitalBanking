package com.infyme.digitalbanking.service;

import com.infyme.digitalbanking.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    String fundTransfer(TransactionDTO transactionDTO);
    List<TransactionDTO> accountStatement(String mobileNumber);
}
