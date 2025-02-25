package com.skillbox.controller;

import com.skillbox.dto.EnrollRequest;
import com.skillbox.model.Course;
import com.skillbox.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> chooseAuthMethod(@RequestParam String userId,
                                              @RequestParam String courseId,
                                              @RequestParam String method) {
        if (userId.isBlank() || courseId.isBlank()) {
            return ResponseEntity.badRequest().body("User ID and Course ID cannot be empty");
        }

        if ("vk".equalsIgnoreCase(method)) {
            Map<String, String> vkUserData = Map.of(
                    "fullName", "Иван Иванов",
                    "email", "ivan.ivanov@example.com",
                    "userId", userId,
                    "courseId", courseId
            );

            EnrollRequest request = new EnrollRequest();
            request.setUserId(userId);
            request.setCourseId(courseId);
            request.setMethod("vk");
            request.setTariff("standard");

            String paymentLink = catalogService.enrollUserToCourse(request);

            return ResponseEntity.ok(Map.of("paymentLink", paymentLink));
        } else if ("manual".equalsIgnoreCase(method)) {
            return ResponseEntity.ok(Map.of("message", "Proceed with manual registration. Send user details to /enroll/manual"));
        } else {
            return ResponseEntity.badRequest().body("Invalid authentication method");
        }
    }


    @PostMapping("/enroll")
    public ResponseEntity<String> enrollUser(@RequestBody EnrollRequest request) {
        String response = catalogService.enrollUserToCourse(request);
        return ResponseEntity.ok(response);
    }
}
