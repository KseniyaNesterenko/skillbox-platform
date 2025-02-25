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
    private String description;
    private List<TariffType> tariffs;

    public Course(String id, String title, String direction, String description, List<TariffType> tariffs) {
        this.id = id;
        this.title = title;
        this.direction = direction;
        this.description = description;
        this.tariffs = tariffs;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDirection() {
        return direction;
    }

    public String getDescription() {
        return description;
    }

    public List<TariffType> getTariffs() {
        return tariffs;
    }
}
