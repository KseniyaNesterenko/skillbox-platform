package com.skillbox.service;

import com.skillbox.client.PaymentClient;
import com.skillbox.dto.EnrollRequest;
import com.skillbox.model.Course;
import com.skillbox.model.TariffType;
import com.skillbox.model.User;
import com.skillbox.repository.CourseRepository;
import com.skillbox.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final PaymentClient paymentClient;

    public CatalogService(UserRepository userRepo, CourseRepository courseRepo, PaymentClient paymentClient) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.paymentClient = paymentClient;
    }

    public List<String> getAllDirections() {
        return courseRepo.findAllDirections();
    }

    public Course getCourseDetails(String courseId) {
        System.out.println("Looking for course with ID: " + courseId);
        return courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }


    public List<Course> getCoursesByDirection(String direction) {
        return courseRepo.findByDirection(direction);
    }

    public User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public String enrollUserToCourse(EnrollRequest request) {
        User user = getUser(request.getUserId());
        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        TariffType requestedTariff;
        try {
            requestedTariff = TariffType.valueOf(request.getTariff().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid tariff: " + request.getTariff());
        }

        if (!course.getTariffs().contains(requestedTariff)) {
            throw new RuntimeException("Invalid tariff");
        }

        if (user.getEnrolledCourses().contains(request.getCourseId())) {
            throw new RuntimeException("User is already enrolled in this course");
        }

        return paymentClient.generatePaymentLink(request.getUserId(), request.getCourseId(), request.getName(), request.getEmail(), request.getTariff());
    }
}
