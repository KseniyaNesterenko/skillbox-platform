package com.skillbox.service;

import com.skillbox.dto.PaymentRequest;
import com.skillbox.dto.PaymentResponse;
import com.skillbox.model.Payment;
import com.skillbox.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        String paymentLink = "https://bank.example.com/pay/" + UUID.randomUUID();

        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setCourseId(request.getCourseId());
        payment.setTariff(request.getTariff());
        payment.setName(request.getName());
        payment.setEmail(request.getEmail());
        payment.setPaymentLink(paymentLink);
        payment.setStatus("PENDING");

        paymentRepository.save(payment);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentLink(paymentLink);
        response.setStatus("PENDING");

        return response;
    }
}