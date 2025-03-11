package com.skillbox.controller;

import com.skillbox.dto.PaymentRequest;
import com.skillbox.dto.PaymentResponse;
import com.skillbox.service.PaymentService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@Tag(name = "Система оплаты", description = "Операции обработки платежей")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Hidden
    @PostMapping("/create")
    @Operation(summary = "Создать платеж", description = "Генерирует ссылку на оплату для пользователя.")
    @ApiResponse(responseCode = "200", description = "Ссылка на оплату успешно создана", content = @Content(mediaType = "text/plain"))
    public String createPayment(@RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @PostMapping("/process")
    @Operation(summary = "Обработка платежа", description = "Проводит платеж пользователя по указанной ссылке. В случае успеха пользователь попадает на крус.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Платеж успешно обработан"),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах платежа", content = @Content)
    })
    public String processPayment(
            @Parameter(description = "ID пользователя") @RequestParam String userId,
            @Parameter(description = "Ссылка на оплату") @RequestParam String paymentLink,
            @Parameter(description = "Сумма платежа") @RequestParam double amount) {
        return paymentService.processPayment(userId, paymentLink, amount);
    }
}
