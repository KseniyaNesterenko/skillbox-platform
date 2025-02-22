package com.skillbox.repository;

import com.skillbox.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {
    @Query(value = "{}", fields = "{ 'direction' : 1 }")
    List<String> findAllDirections();
    Optional<Course> findById(String id);
    @Query("{ 'direction': ?0 }")
    List<Course> findByDirection(String direction);
}
