package com.skillbox.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import lombok.Data;

@Document(collection = "courses")
@Data
public class Course {
    @Id
    private String id;
    private String title;
    private String direction;
    private List<String> tariffs;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirection() {
        return direction;
    }

    public List<String> getTariffs() {
        return tariffs;
    }
}
