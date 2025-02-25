package com.skillbox.dto;
import lombok.Data;

@Data
public class EnrollRequest {
    private String userId;
    private String courseId;
    private String tariff;
    private String method;
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMethod() {
        return method;
    }
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
