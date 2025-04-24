package com.example.liquorstore.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentDto implements Serializable {
    private String mobileNumber;
    private BigDecimal amount;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


}
