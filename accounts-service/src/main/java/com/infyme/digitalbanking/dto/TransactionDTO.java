package com.infyme.digitalbanking.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long transactionId;

    @NotNull
    private Long senderAccountNumber;

    @NotNull
    private Long receiverAccountNumber;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    private String paidFrom;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    private String paidTo;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    private String remarks;
    private String modeOfTransaction;
    private LocalDateTime transactionDateTime;

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public Long getSenderAccountNumber() { return senderAccountNumber; }
    public void setSenderAccountNumber(Long senderAccountNumber) { this.senderAccountNumber = senderAccountNumber; }
    public Long getReceiverAccountNumber() { return receiverAccountNumber; }
    public void setReceiverAccountNumber(Long receiverAccountNumber) { this.receiverAccountNumber = receiverAccountNumber; }
    public String getPaidFrom() { return paidFrom; }
    public void setPaidFrom(String paidFrom) { this.paidFrom = paidFrom; }
    public String getPaidTo() { return paidTo; }
    public void setPaidTo(String paidTo) { this.paidTo = paidTo; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getModeOfTransaction() { return modeOfTransaction; }
    public void setModeOfTransaction(String modeOfTransaction) { this.modeOfTransaction = modeOfTransaction; }
    public LocalDateTime getTransactionDateTime() { return transactionDateTime; }
    public void setTransactionDateTime(LocalDateTime transactionDateTime) { this.transactionDateTime = transactionDateTime; }
}
