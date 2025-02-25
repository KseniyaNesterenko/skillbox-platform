package com.skillbox.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String userId;
    private String courseId;
    private String tariff;
    private String name;
    private String email;
    private String paymentLink;
    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime createdAt = LocalDateTime.now();


    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTariff() {
        return tariff;
    }

    public String getPaymentLink() {
        return paymentLink;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
