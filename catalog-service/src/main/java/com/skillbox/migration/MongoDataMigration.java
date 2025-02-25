package com.skillbox.migration;

import com.skillbox.model.TariffType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import com.skillbox.model.Course;

import java.util.List;

@Component
public class MongoDataMigration implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (mongoTemplate.count(new Query(Criteria.where("title").exists(true)), Course.class) == 0) {
            List<TariffType> allTariffs = List.of(TariffType.STARTER, TariffType.BASIC, TariffType.MAXIMUM_SUPPORT);

            Course course1 = new Course(String.valueOf(1), "Test Course", "Test", "It's a test course.", allTariffs);
            Course course2 = new Course(String.valueOf(2), "Another Course", "Another_test", "It's a test course too.",allTariffs);

            mongoTemplate.save(course1);
            mongoTemplate.save(course2);

            System.out.println("Initial courses added.");
        }
    }
}
