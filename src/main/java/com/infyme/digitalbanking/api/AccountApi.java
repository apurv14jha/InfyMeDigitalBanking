package com.infyme.digitalbanking.api;

import com.infyme.digitalbanking.dto.BankAccountDTO;
import com.infyme.digitalbanking.dto.LinkAccountRequest;
import com.infyme.digitalbanking.dto.TransactionDTO;
import com.infyme.digitalbanking.service.BankAccountService;
import com.infyme.digitalbanking.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin
public class AccountApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountApi.class);

    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    public AccountApi(BankAccountService bankAccountService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@Valid @RequestBody BankAccountDTO accountDTO) {
        LOGGER.info("createAccount endpoint called");
        return ResponseEntity.ok(bankAccountService.createAccount(accountDTO));
    }

    @GetMapping("/{mobileNo}")
    public ResponseEntity<List<BankAccountDTO>> listAccounts(@PathVariable String mobileNo) {
        LOGGER.info("listAccounts endpoint called");
        return ResponseEntity.ok(bankAccountService.listAccounts(mobileNo));
    }

    @PostMapping("/{mobileNo}")
    public ResponseEntity<String> linkAccount(@PathVariable String mobileNo, @Valid @RequestBody LinkAccountRequest request) {
        LOGGER.info("linkAccount endpoint called");
        return ResponseEntity.ok(bankAccountService.linkAccount(mobileNo, request.getAccountNumber(), null));
    }

    @PostMapping("/{mobileNo}/otp")
    public ResponseEntity<String> linkAccountWithOtp(@PathVariable String mobileNo, @Valid @RequestBody LinkAccountRequest request) {
        LOGGER.info("linkAccountWithOtp endpoint called");
        return ResponseEntity.ok(bankAccountService.linkAccount(mobileNo, request.getAccountNumber(), request.getOtp()));
    }

    @GetMapping("/balance/{mobileNo}")
    public ResponseEntity<String> checkBalance(@PathVariable String mobileNo, @RequestParam("accountNo") Long accountNo) {
        LOGGER.info("checkBalance endpoint called");
        return ResponseEntity.ok(bankAccountService.checkBalance(mobileNo, accountNo));
    }

    @PatchMapping("/fundtransfer")
    public ResponseEntity<String> fundTransfer(@Valid @RequestBody TransactionDTO transactionDTO) {
        LOGGER.info("fundTransfer endpoint called");
        return ResponseEntity.ok(transactionService.fundTransfer(transactionDTO));
    }

    @GetMapping("/statement/{mobileNo}")
    public ResponseEntity<List<TransactionDTO>> accountStatement(@PathVariable String mobileNo) {
        LOGGER.info("accountStatement endpoint called");
        return ResponseEntity.ok(transactionService.accountStatement(mobileNo));
    }
}
