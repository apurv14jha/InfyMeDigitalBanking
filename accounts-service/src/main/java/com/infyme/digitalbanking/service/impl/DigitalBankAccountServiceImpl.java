package com.infyme.digitalbanking.service.impl;

import com.infyme.digitalbanking.repository.DigitalBankAccountRepository;
import com.infyme.digitalbanking.service.DigitalBankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DigitalBankAccountServiceImpl implements DigitalBankAccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalBankAccountServiceImpl.class);
    private final DigitalBankAccountRepository digitalBankAccountRepository;

    public DigitalBankAccountServiceImpl(DigitalBankAccountRepository digitalBankAccountRepository) {
        this.digitalBankAccountRepository = digitalBankAccountRepository;
    }

    @Override
    public String generateDigitalBankingId() {
        LOGGER.info("generateDigitalBankingId called");
        long next = digitalBankAccountRepository.count() + 1001;
        return "W_" + next;
    }
}
