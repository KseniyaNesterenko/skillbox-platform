package com.skillbox.controller;

import com.skillbox.dto.EnrollRequest;
import com.skillbox.model.Course;
import com.skillbox.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/directions")
    public ResponseEntity<List<String>> getAllDirections() {
        List<String> directions = catalogService.getAllDirections();
        if (directions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directions);
    }

    @GetMapping("/courses/{direction}")
    public ResponseEntity<List<String>> getCourses(@PathVariable(name = "direction") String direction) {
        List<String> courseTitles = catalogService.getCoursesByDirection(direction)
                .stream()
                .map(Course::getTitle)
                .toList();

        if (courseTitles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(courseTitles);
    }


    @GetMapping("/course/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable String courseId) {
        Course course = catalogService.getCourseDetails(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }


    @PostMapping("/enroll/auth-method")
    public ResponseEntity<?> chooseAuthMethod(@RequestBody EnrollRequest request) {
        if (request.getUserId().isBlank() || request.getCourseId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID и Course ID не могут быть пустыми");
        }
        if ("vk".equalsIgnoreCase(request.getMethod())) {
            String userName = "Дмитрий Борисович Афанасьев";
            String userEmail = "dima@example.com";

            request.setName(userName);
            request.setEmail(userEmail);

            String paymentLink = catalogService.enrollUserToCourse(request);
            return ResponseEntity.ok(Map.of("paymentLink", paymentLink));
        }
        else if ("manual".equalsIgnoreCase(request.getMethod())) {
            return ResponseEntity.ok(Map.of("message", "Proceed with manual registration. Send user details to /enroll/manual"));
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Неверный метод аутентификации: " + request.getMethod());
        }
    }


    @PostMapping("/enroll")
    public ResponseEntity<Map<String, String>> enrollUser(@RequestBody EnrollRequest request) {
        String response = catalogService.enrollUserToCourse(request);
        return ResponseEntity.ok(Map.of("message", response));
    }
}
