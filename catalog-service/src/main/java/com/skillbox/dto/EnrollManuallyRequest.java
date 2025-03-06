package com.skillbox.dto;
import lombok.Data;

@Data
public class EnrollManuallyRequest {
    private String userId;
    private String courseId;
    private String tariff;
    private String method;
    private String name;
    private String email;
}
