package com.example.aistudio.web;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        String requestId,
        String code,
        String message,
        Map<String, Object> details
) {}
