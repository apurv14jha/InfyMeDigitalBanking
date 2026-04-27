package com.infyme.digitalbanking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "digital_bank_account")
public class DigitalBankAccountEntity {

    @Id
    @Column(name = "digital_banking_id")
    private String digitalBankingId;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number", nullable = false)
    private BankAccountEntity account;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    public String getDigitalBankingId() { return digitalBankingId; }
    public void setDigitalBankingId(String digitalBankingId) { this.digitalBankingId = digitalBankingId; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public BankAccountEntity getAccount() { return account; }
    public void setAccount(BankAccountEntity account) { this.account = account; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
}
