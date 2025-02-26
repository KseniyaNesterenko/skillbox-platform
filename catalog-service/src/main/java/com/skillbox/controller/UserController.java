package com.skillbox.controller;

import com.skillbox.model.User;
import com.skillbox.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/{userId}/enroll/{courseId}")
    public ResponseEntity<String> enrollUserInCourse(@PathVariable String userId, @PathVariable String courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEnrolledCourses() == null) {
            user.setEnrolledCourses(new ArrayList<>());
        }

        if (user.getEnrolledCourses().contains(courseId)) {
            return ResponseEntity.badRequest().body("User is already enrolled in course: " + courseId);
        }

        user.getEnrolledCourses().add(courseId);
        userRepository.save(user);

        return ResponseEntity.ok("User enrolled in course: " + courseId);
    }

}
