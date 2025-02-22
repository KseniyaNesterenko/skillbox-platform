package com.skillbox.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import java.util.Map;

@Component
public class PaymentClient {
    private final RestTemplate restTemplate;
    private final String paymentServiceUrl = "http://payment-service/api/payment";

    public PaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generatePaymentLink(String userId, String courseId, String tariff) {
        Map<String, String> requestBody = Map.of(
                "userId", userId,
                "courseId", courseId,
                "tariff", tariff
        );

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(paymentServiceUrl, requestBody, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Payment service error: " + e.getMessage());
        } catch (ResourceAccessException e) {
            throw new RuntimeException("Payment service is unreachable. Try again later.");
        }
    }
}
