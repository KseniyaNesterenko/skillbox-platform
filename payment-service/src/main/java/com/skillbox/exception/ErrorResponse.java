package com.skillbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ErrorResponse {

    public static ResponseStatusException bankNotFound(String userId) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Банк пользователя " + userId + " не найден");
    }

    public static ResponseStatusException paymentLinkNotFound(String paymentLink) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Ссылка " + paymentLink + " некорректна!");
    }

    public static ResponseStatusException paymentLinkExpired() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Ссылка на оплату просрочена!");
    }

    public static ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }

    public static ResponseStatusException internalError(String message) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
