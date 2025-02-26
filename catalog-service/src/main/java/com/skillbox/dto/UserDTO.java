package com.skillbox.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private List<String> enrolledCourses;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }
}
