package com.skillbox.controller;

import com.skillbox.model.User;
import com.skillbox.repository.UserRepository;
import com.skillbox.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Hidden
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

    @Operation(summary = "Информация о пользователе", description = "Возвращает информацию о пользователе и его курсах")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные о пользователе успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Получение задания по курсу", description = "Возвращает описание задания курса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задание успешно получено"),
            @ApiResponse(responseCode = "404", description = "Задание не найдено")
    })
    @GetMapping("/{userId}/courses/{courseId}/task-description")
    public ResponseEntity<String> getTaskDescription(
            @PathVariable String userId,
            @PathVariable String courseId) {
        String description = userService.getTaskDescriptionByCourseId(userId, courseId);
        return ResponseEntity.ok(description);
    }

}
