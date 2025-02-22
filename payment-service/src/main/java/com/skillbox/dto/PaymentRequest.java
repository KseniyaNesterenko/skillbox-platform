package com.skillbox.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String userId;
    private String courseId;
    private String tariff;

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTariff() {
        return tariff;
    }
}
