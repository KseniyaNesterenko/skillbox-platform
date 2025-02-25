package com.skillbox.migration;

import com.skillbox.model.TariffType;
import com.skillbox.model.Course;
import com.skillbox.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoDataMigration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        addInitialCourses();
        addInitialUsers();
    }

    private void addInitialCourses() {
        if (mongoTemplate.count(new Query(Criteria.where("title").exists(true)), Course.class) == 0) {
            List<TariffType> allTariffs = List.of(TariffType.STARTER, TariffType.BASIC, TariffType.MAXIMUM_SUPPORT);

            Course course1 = new Course("1", "Test Course", "Test", "It's a test course.", allTariffs);
            Course course2 = new Course("2", "Another Course", "Another_test", "It's a test course too.", allTariffs);

            mongoTemplate.save(course1);
            mongoTemplate.save(course2);

            System.out.println("📌 Initial courses added.");
        }
    }

    private void addInitialUsers() {
        if (mongoTemplate.count(new Query(Criteria.where("email").exists(true)), User.class) == 0) {
            User user1 = new User();
            user1.setName("Иван Иванов");
            user1.setEmail("ivan.ivanov@example.com");
            user1.setEnrolledCourses(List.of());

            User user2 = new User();
            user2.setName("Анна Петрова");
            user2.setEmail("anna.petrova@example.com");
            user2.setEnrolledCourses(List.of());

            mongoTemplate.save(user1);
            mongoTemplate.save(user2);

            System.out.println("📌 Initial users added.");
        }
    }
}
