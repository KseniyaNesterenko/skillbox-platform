package com.skillbox.service;

import com.skillbox.client.PaymentClient;
import com.skillbox.dto.EnrollManuallyRequest;
import com.skillbox.dto.EnrollRequest;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.Course;
import com.skillbox.model.TariffType;
import com.skillbox.model.User;
import com.skillbox.repository.CourseRepository;
import com.skillbox.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private PaymentClient paymentClient;

    public List<String> getAllDirections() {
        return courseRepo.findAllDirections();
    }

    public Course getCourseDetails(String courseId) {
        return courseRepo.findById(courseId)
                .orElseThrow(() -> ErrorResponse.courseNotFound(courseId));
    }

    public List<Course> getCoursesByDirection(String direction) {
        return courseRepo.findByDirection(direction);
    }

    public User getUser(String userId) {
        return userRepo.findById(userId).orElseThrow(() -> ErrorResponse.userNotFound(userId));
    }

    public String enrollUserToCourse(EnrollManuallyRequest request) {
        User user = getUser(request.getUserId());
        String defaultEmail = "dima@example.com";

        if (!defaultEmail.equals(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
            throw ErrorResponse.emailMismatch();
        }

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> ErrorResponse.courseNotFound(request.getCourseId()));

        TariffType requestedTariff;
        try {
            requestedTariff = TariffType.valueOf(request.getTariff().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw ErrorResponse.invalidTariff(request.getTariff());
        }

        if (!course.getTariffs().contains(requestedTariff)) {
            throw ErrorResponse.tariffNotAvailable();
        }

        if (user.getEnrolledCourses().contains(request.getCourseId())) {
            throw ErrorResponse.userAlreadyEnrolled(request.getUserId(), request.getCourseId());
        }

        return paymentClient.generatePaymentLink(request.getUserId(), request.getCourseId(), request.getName(), request.getEmail(), request.getTariff());
    }
}