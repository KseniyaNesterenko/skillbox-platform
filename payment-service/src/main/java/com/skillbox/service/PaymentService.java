package com.skillbox.service;

import com.skillbox.dto.PaymentRequest;
import com.skillbox.dto.PaymentResponse;
import com.skillbox.model.Bank;
import com.skillbox.model.Payment;
import com.skillbox.repository.BankRepository;
import com.skillbox.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private BankRepository bankRepository;

    public PaymentService(PaymentRepository paymentRepository, BankRepository bankRepository) {
        this.paymentRepository = paymentRepository;
        this.bankRepository = bankRepository;
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

    public String processPayment(String userId, String paymentLink, double amount) {
        Bank bank = bankRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User bank account not found"));

        if (bank.getBalance() >= amount) {
            bank.setBalance(bank.getBalance() - amount);
            bankRepository.save(bank);

            Payment payment = paymentRepository.findByPaymentLink(paymentLink)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            return "SUCCESS";
        } else {
            return "INSUFFICIENT_FUNDS";
        }
    }
}