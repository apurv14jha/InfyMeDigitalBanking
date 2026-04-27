package com.infyme.digitalbanking.dto;

import jakarta.validation.constraints.NotNull;

public class LinkAccountRequest {
    @NotNull
    private Long accountNumber;
    private Integer otp;

    public Long getAccountNumber() { return accountNumber; }
    public void setAccountNumber(Long accountNumber) { this.accountNumber = accountNumber; }
    public Integer getOtp() { return otp; }
    public void setOtp(Integer otp) { this.otp = otp; }
}
