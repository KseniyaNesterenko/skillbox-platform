package com.skillbox.controller;

import com.skillbox.dto.EnrollRequest;
import com.skillbox.model.Course;
import com.skillbox.service.CatalogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/directions")
    public List<String> getAllDirections() {
        return catalogService.getAllDirections();
    }

    @GetMapping("/courses/{direction}")
    public List<Course> getCourses(@PathVariable String direction) {
        return catalogService.getCoursesByDirection(direction);
    }

    @GetMapping("/course/{courseId}")
    public Course getCourseDetails(@PathVariable String courseId) {
        return catalogService.getCourseDetails(courseId);
    }

    @PostMapping("/enroll")
    public String enrollUser(@RequestBody EnrollRequest request) {
        return catalogService.enrollUserToCourse(request);
    }
}
