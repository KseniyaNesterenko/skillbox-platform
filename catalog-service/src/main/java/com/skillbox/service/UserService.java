package com.skillbox.service;

import com.skillbox.model.CourseTask;
import com.skillbox.model.User;
import com.skillbox.repository.CourseTaskRepository;
import com.skillbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseTaskRepository courseTaskRepository;

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public String getTaskDescriptionByCourseId(String userId, String courseId) {
        User user = getUserById(userId);
        if (user != null && user.getEnrolledCourses().contains(courseId)) {
            List<CourseTask> tasks = courseTaskRepository.findByCourseId(courseId);
            if (!tasks.isEmpty()) {
                return tasks.get(0).getTaskDescription();
            }
            return "Для курса не найдено заданий.";
        }
        return "Доступ к заданиям курса предоставляется после оплаты!";
    }
}
