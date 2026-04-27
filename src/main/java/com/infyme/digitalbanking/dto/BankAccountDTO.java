package com.infyme.digitalbanking.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BankAccountDTO {

    private Long accountNumber;

    @NotNull(message = "{bank.name.not.null}")
    @Size(min = 5, max = 15, message = "{bank.name.size}")
    private String bankName;

    @NotNull(message = "{balance.not.null}")
    @DecimalMin(value = "0.0", inclusive = true, message = "{balance.non.negative}")
    private BigDecimal balance;

    @NotNull(message = "{account.type.not.null}")
    @Size(min = 3, max = 15, message = "{account.type.size}")
    private String accountType;

    @NotNull(message = "{ifsc.not.null}")
    @Size(min = 5, max = 15, message = "{ifsc.size}")
    private String ifscCode;

    @NotNull(message = "{opening.date.not.null}")
    @Past(message = "{opening.date.past}")
    private LocalDate openingDate;

    @NotNull(message = "{mobile.not.null}")
    @Pattern(regexp = "^[0-9]{10}$", message = "{mobile.size}")
    private String mobileNumber;

    public Long getAccountNumber() { return accountNumber; }
    public void setAccountNumber(Long accountNumber) { this.accountNumber = accountNumber; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }
    public LocalDate getOpeningDate() { return openingDate; }
    public void setOpeningDate(LocalDate openingDate) { this.openingDate = openingDate; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}
