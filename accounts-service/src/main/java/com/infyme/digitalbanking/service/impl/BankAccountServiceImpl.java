package com.infyme.digitalbanking.service.impl;

import com.infyme.digitalbanking.dto.BankAccountDTO;
import com.infyme.digitalbanking.entity.BankAccountEntity;
import com.infyme.digitalbanking.entity.DigitalBankAccountEntity;
import com.infyme.digitalbanking.exception.ExceptionConstants;
import com.infyme.digitalbanking.exception.InfyMeDigitalBankingException;
import com.infyme.digitalbanking.repository.AccountRepository;
import com.infyme.digitalbanking.repository.DigitalBankAccountRepository;
import com.infyme.digitalbanking.service.BankAccountService;
import com.infyme.digitalbanking.service.DigitalBankAccountService;
import com.infyme.digitalbanking.util.OTPUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final AccountRepository accountRepository;
    private final DigitalBankAccountRepository digitalBankAccountRepository;
    private final DigitalBankAccountService digitalBankAccountService;
    private final OTPUtility otpUtility;
    private final MessageSource messageSource;

    public BankAccountServiceImpl(AccountRepository accountRepository,
                                  DigitalBankAccountRepository digitalBankAccountRepository,
                                  DigitalBankAccountService digitalBankAccountService,
                                  OTPUtility otpUtility,
                                  MessageSource messageSource) {
        this.accountRepository = accountRepository;
        this.digitalBankAccountRepository = digitalBankAccountRepository;
        this.digitalBankAccountService = digitalBankAccountService;
        this.otpUtility = otpUtility;
        this.messageSource = messageSource;
    }

    @Override
    public String createAccount(BankAccountDTO accountDTO) {
        LOGGER.info("createAccount called");
        BankAccountEntity entity = new BankAccountEntity();
        Long nextAccountNumber = accountRepository.findTopByOrderByAccountNumberDesc()
                .map(BankAccountEntity::getAccountNumber)
                .map(v -> v + 1)
                .orElse(512345678L);
        entity.setAccountNumber(nextAccountNumber);
        entity.setBankName(accountDTO.getBankName());
        entity.setBalance(accountDTO.getBalance());
        entity.setAccountType(accountDTO.getAccountType());
        entity.setIfscCode(accountDTO.getIfscCode());
        entity.setOpeningDate(accountDTO.getOpeningDate());
        entity.setMobileNumber(accountDTO.getMobileNumber());
        return String.valueOf(accountRepository.save(entity).getAccountNumber());
    }

    @Override
    public List<BankAccountDTO> listAccounts(String mobileNumber) {
        LOGGER.info("listAccounts called");
        List<BankAccountEntity> entities = accountRepository.findByMobileNumber(mobileNumber);
        if (entities.isEmpty()) {
            throw new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACCOUNTS_FOUND));
        }
        return entities.stream().map(this::toDto).toList();
    }

    @Override
    public String linkAccount(String mobileNumber, Long accountNumber, Integer otp) {
        LOGGER.info("linkAccount called");
        BankAccountEntity account = accountRepository.findById(accountNumber)
                .filter(a -> a.getMobileNumber().equals(mobileNumber))
                .orElseThrow(() -> new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACCOUNTS_FOUND)));

        if (otp != null && otp != otpUtility.generateOtp(mobileNumber)) {
            throw new InfyMeDigitalBankingException(msg(ExceptionConstants.OTP_DOESNOT_MATCH));
        }

        if (!digitalBankAccountRepository.existsByMobileNumberAndAccount_AccountNumber(mobileNumber, accountNumber)) {
            DigitalBankAccountEntity linked = new DigitalBankAccountEntity();
            linked.setDigitalBankingId(digitalBankAccountService.generateDigitalBankingId());
            linked.setMobileNumber(mobileNumber);
            linked.setAccount(account);
            linked.setAccountType(account.getAccountType());
            digitalBankAccountRepository.save(linked);
        }
        return "Account linked successfully";
    }

    @Override
    public String checkBalance(String mobileNumber, Long accountNumber) {
        LOGGER.info("checkBalance called");
        boolean linked = digitalBankAccountRepository.existsByMobileNumberAndAccount_AccountNumber(mobileNumber, accountNumber);
        if (!linked) {
            throw new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACCOUNT_IS_LINKED));
        }

        BankAccountEntity account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new InfyMeDigitalBankingException(msg(ExceptionConstants.NO_ACCOUNTS_FOUND)));
        return account.getBalance().toPlainString();
    }

    private BankAccountDTO toDto(BankAccountEntity entity) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setBankName(entity.getBankName());
        dto.setBalance(entity.getBalance());
        dto.setAccountType(entity.getAccountType());
        dto.setIfscCode(entity.getIfscCode());
        dto.setOpeningDate(entity.getOpeningDate());
        dto.setMobileNumber(entity.getMobileNumber());
        return dto;
    }

    private String msg(ExceptionConstants constant) {
        return messageSource.getMessage(constant.getCode(), null, Locale.getDefault());
    }
}
