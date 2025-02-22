package com.skillbox.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentLink;
    private String status;

    public String getPaymentLink() {
        return paymentLink;
    }

    public String getStatus() {
        return status;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
