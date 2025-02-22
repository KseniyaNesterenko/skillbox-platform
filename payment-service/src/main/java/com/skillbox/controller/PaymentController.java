package com.skillbox.controller;

import com.skillbox.dto.PaymentRequest;
import com.skillbox.dto.PaymentResponse;
import com.skillbox.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public PaymentResponse createPayment(@RequestBody PaymentRequest request) {
        return paymentService.createPayment(request);
    }
}