package com.skillbox.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String userId;
    private String courseId;
    private String tariff;
    private String name;
    private String email;

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTariff() {
        return tariff;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
