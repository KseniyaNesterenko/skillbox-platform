package com.skillbox.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EnrollRequest extends EnrollManuallyRequest {
    private String method;
    @JsonIgnore
    @Override
    public String getName() {
        return super.getName();
    }

    @JsonIgnore
    @Override
    public String getEmail() {
        return super.getEmail();
    }
}
