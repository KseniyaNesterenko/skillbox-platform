package com.skillbox.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import lombok.Data;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private List<String> enrolledCourses;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }
}
