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

            Course course1 = new Course("1", "Python-разработчик", "Программирование", "тут типа описание", allTariffs);
            Course course2 = new Course("2", "Инженер по тестированию", "Программирование", "тут тоже типа описание", allTariffs);
            Course course3 = new Course("3", "Коммерческий иллюстратор", "Дизайн", "а это третье типа описание", allTariffs);

            mongoTemplate.save(course1);
            mongoTemplate.save(course2);

            System.out.println("📌 Initial courses added.");
        }
    }

    private void addInitialUsers() {
        if (mongoTemplate.count(new Query(Criteria.where("email").exists(true)), User.class) == 0) {
            User user1 = new User();
            user1.setId(String.valueOf(1));
            user1.setName("Дмитрий Борисович");
            user1.setEmail("opd@example.com");
            user1.setEnrolledCourses(List.of());

            User user2 = new User();
            user2.setId(String.valueOf(2));
            user2.setName("Борис Дмитриевич");
            user2.setEmail("dopsa@example.com");
            user2.setEnrolledCourses(List.of());

            User user3 = new User();
            user3.setId(String.valueOf(3));
            user3.setName("Афанасий");
            user3.setEmail("dima@example.com");
            user3.setEnrolledCourses(List.of());

            mongoTemplate.save(user1);
            mongoTemplate.save(user2);
            mongoTemplate.save(user3);

            System.out.println("📌 Initial users added.");
        }
    }
}
