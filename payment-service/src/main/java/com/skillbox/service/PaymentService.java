package com.skillbox.service;

import com.skillbox.dto.PaymentRequest;
import com.skillbox.dto.PaymentResponse;
import com.skillbox.exception.ErrorResponse;
import com.skillbox.model.Bank;
import com.skillbox.model.Payment;
import com.skillbox.repository.BankRepository;
import com.skillbox.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private RestTemplate restTemplate;

    private String catalogServiceUrl =  "http://catalog-service:8080";

    public String createPayment(PaymentRequest request) {
        String paymentLink = "https://bank.example.com/pay/" + UUID.randomUUID();

        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setCourseId(request.getCourseId());
        payment.setTariff(request.getTariff());
        payment.setName(request.getName());
        payment.setEmail(request.getEmail());
        payment.setPaymentLink(paymentLink);
        payment.setStatus("PENDING");
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        paymentRepository.save(payment);

        return paymentLink;
    }


    public String processPayment(String userId, String paymentLink, double amount) {
        Bank bank = bankRepository.findByUserId(userId)
                .orElseThrow(() -> ErrorResponse.bankNotFound(userId));

        if (amount != (int) amount) {
            throw ErrorResponse.invalidAmountType();
        }

        if (amount <= 0) {
            throw ErrorResponse.negativeAmount();
        }

        Payment payment = paymentRepository.findByPaymentLink(paymentLink)
                .orElseThrow(() -> ErrorResponse.paymentLinkNotFound(paymentLink));

        if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw ErrorResponse.paymentLinkExpired();
        }

        if ("SUCCESS".equals(payment.getStatus())) {
            throw ErrorResponse.paymentAlreadyProcessed(paymentLink);
        }

        if (bank.getBalance() >= amount) {
            bank.setBalance(bank.getBalance() - amount);
            bankRepository.save(bank);

            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            String enrollUrl = catalogServiceUrl + "/users/" + userId + "/enroll/" + payment.getCourseId();
            restTemplate.put(enrollUrl, null);

            return "SUCCESS";
        } else {
            return "INSUFFICIENT_FUNDS";
        }
    }

}