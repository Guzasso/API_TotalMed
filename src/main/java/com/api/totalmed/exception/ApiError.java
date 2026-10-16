package com.api.totalmed.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> detalhes
) {
    public ApiError(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, List.of());
    }
    public ApiError(int status, String error, String message, List<String> detalhes) {
        this(LocalDateTime.now(), status, error, message, detalhes);
    }
}