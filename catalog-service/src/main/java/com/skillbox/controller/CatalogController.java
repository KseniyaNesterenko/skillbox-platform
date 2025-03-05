package com.skillbox.controller;

import com.skillbox.dto.EnrollRequest;
import com.skillbox.model.Course;
import com.skillbox.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
@Tag(name = "Каталог курсов", description = "API для работы с курсами и направлениями")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Operation(
            summary = "Получить все направления обучения",
            description = "Возвращает список доступных направлений обучения."
    )
    @GetMapping("/directions")
    public ResponseEntity<List<String>> getAllDirections() {
        List<String> directions = catalogService.getAllDirections();
        if (directions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(directions);
    }

    @Operation(
            summary = "Получить список курсов по направлению",
            description = "Возвращает список названий курсов для указанного направления.",
            parameters = {
                    @Parameter(name = "direction", description = "Название направления", example = "Программирование")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список найденных курсов"),
                    @ApiResponse(responseCode = "404", description = "Курсы не найдены")
            }
    )
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

    @Operation(
            summary = "Получить детали курса",
            description = "Возвращает подробную информацию о курсе по его ID.",
            parameters = {
                    @Parameter(name = "courseId", description = "ID курса", example = "java-101")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Детали курса",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Course.class))),
                    @ApiResponse(responseCode = "404", description = "Курс не найден")
            }
    )
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable String courseId) {
        Course course = catalogService.getCourseDetails(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(course);
    }

    @Operation(
            summary = "Выбрать метод аутентификации для записи на курс",
            description = "Позволяет пользователю выбрать метод аутентификации (VK или manual) перед записью на курс.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Ответ с деталями аутентификации"),
                    @ApiResponse(responseCode = "400", description = "Неверный метод аутентификации")
            }
    )
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

    @Operation(
            summary = "Записать пользователя на курс",
            description = "Принимает данные от пользователя и возвращает ему ссылку на оплату.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные о пользователе и курсе",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EnrollRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Запись успешна"),
                    @ApiResponse(responseCode = "400", description = "Ошибка при записи")
            }
    )
    @PostMapping("/enroll")
    public ResponseEntity<Map<String, String>> enrollUser(@RequestBody EnrollRequest request) {
        String response = catalogService.enrollUserToCourse(request);
        return ResponseEntity.ok(Map.of("message", response));
    }
}
