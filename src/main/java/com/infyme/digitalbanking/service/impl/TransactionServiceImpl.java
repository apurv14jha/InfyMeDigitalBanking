package com.infyme.digitalbanking.service.impl;

import com.infyme.digitalbanking.dto.TransactionDTO;
import com.infyme.digitalbanking.entity.BankAccountEntity;
import com.infyme.digitalbanking.entity.TransactionEntity;
import com.infyme.digitalbanking.exception.ExceptionConstants;
import com.infyme.digitalbanking.exception.InfyMeDigitalBankingException;
import com.infyme.digitalbanking.repository.AccountRepository;
import com.infyme.digitalbanking.repository.DigitalBankAccountRepository;
import com.infyme.digitalbanking.repository.TransactionRepository;
import com.infyme.digitalbanking.service.TransactionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final DigitalBankAccountRepository digitalBankAccountRepository;
    private final MessageSource messageSource;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository,
                                  DigitalBankAccountRepository digitalBankAccountRepository,
                                  MessageSource messageSource) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.digitalBankAccountRepository = digitalBankAccountRepository;
        this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public String fundTransfer(TransactionDTO transactionDTO) {
        LOGGER.info("fundTransfer called");

        if (!digitalBankAccountRepository.existsByMobileNumberAndAccount_AccountNumber(transactionDTO.getPaidFrom(), transactionDTO.getSenderAccountNumber())) {
            throw new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACCOUNT_IS_LINKED));
        }

        BankAccountEntity sender = accountRepository.findById(transactionDTO.getSenderAccountNumber())
                .orElseThrow(() -> new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACCOUNTS_FOUND)));

        BankAccountEntity receiver = accountRepository.findById(transactionDTO.getReceiverAccountNumber())
                .orElseThrow(() -> new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACCOUNTS_FOUND)));

        if (sender.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new InfyMeDigitalBankingException(msg(ExceptionConstants.INSUFFICIENT_FUNDS));
        }

        sender.setBalance(sender.getBalance().subtract(transactionDTO.getAmount()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.getAmount()));
        accountRepository.save(sender);
        accountRepository.save(receiver);

        Long nextTransactionId = transactionRepository.findTopByOrderByTransactionIdDesc()
                .map(TransactionEntity::getTransactionId)
                .map(v -> v + 1)
                .orElse(1234543L);

        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId(nextTransactionId);
        entity.setModeOfTransaction("Fund Transfer");
        entity.setPaidFrom(transactionDTO.getPaidFrom());
        entity.setPaidTo(transactionDTO.getPaidTo());
        entity.setSenderAccountNumber(transactionDTO.getSenderAccountNumber());
        entity.setReceiverAccountNumber(transactionDTO.getReceiverAccountNumber());
        entity.setAmount(transactionDTO.getAmount());
        entity.setRemarks(transactionDTO.getRemarks() == null ? "Fund transfer" : transactionDTO.getRemarks());
        entity.setTransactionDateTime(LocalDateTime.now());
        transactionRepository.save(entity);

        return "Transfer successful";
    }

    @Override
    public List<TransactionDTO> accountStatement(String mobileNumber) {
        LOGGER.info("accountStatement called");
        List<TransactionDTO> transactions = transactionRepository.findByPaidFromOrPaidToOrderByTransactionDateTimeDesc(mobileNumber, mobileNumber)
                .stream().map(this::toDto).toList();
        if (transactions.isEmpty()) {
            throw new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACTIVE_TRANSACTIONS));
        }
        return transactions;
    }

    private TransactionDTO toDto(TransactionEntity entity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(entity.getTransactionId());
        dto.setModeOfTransaction(entity.getModeOfTransaction());
        dto.setPaidFrom(entity.getPaidFrom());
        dto.setPaidTo(entity.getPaidTo());
        dto.setSenderAccountNumber(entity.getSenderAccountNumber());
        dto.setReceiverAccountNumber(entity.getReceiverAccountNumber());
        dto.setAmount(entity.getAmount());
        dto.setRemarks(entity.getRemarks());
        dto.setTransactionDateTime(entity.getTransactionDateTime());
        return dto;
    }

    private String msg(ExceptionConstants constant) {
        return messageSource.getMessage(constant.getCode(), null, Locale.getDefault());
    }
}
