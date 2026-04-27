package com.infyme.digitalbanking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "mode_of_transaction", nullable = false)
    private String modeOfTransaction;

    @Column(name = "paid_to", nullable = false)
    private String paidTo;

    @Column(name = "receiver_account_number", nullable = false)
    private Long receiverAccountNumber;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date_time", nullable = false)
    private LocalDateTime transactionDateTime;

    @Column(nullable = false)
    private String remarks;

    @Column(name = "paid_from", nullable = false)
    private String paidFrom;

    @Column(name = "sender_account_number", nullable = false)
    private Long senderAccountNumber;

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getModeOfTransaction() { return modeOfTransaction; }
    public void setModeOfTransaction(String modeOfTransaction) { this.modeOfTransaction = modeOfTransaction; }
    public String getPaidTo() { return paidTo; }
    public void setPaidTo(String paidTo) { this.paidTo = paidTo; }
    public Long getReceiverAccountNumber() { return receiverAccountNumber; }
    public void setReceiverAccountNumber(Long receiverAccountNumber) { this.receiverAccountNumber = receiverAccountNumber; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getTransactionDateTime() { return transactionDateTime; }
    public void setTransactionDateTime(LocalDateTime transactionDateTime) { this.transactionDateTime = transactionDateTime; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getPaidFrom() { return paidFrom; }
    public void setPaidFrom(String paidFrom) { this.paidFrom = paidFrom; }
    public Long getSenderAccountNumber() { return senderAccountNumber; }
    public void setSenderAccountNumber(Long senderAccountNumber) { this.senderAccountNumber = senderAccountNumber; }
}
