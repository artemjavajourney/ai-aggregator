package com.example.aistudio.web.dto;

import com.example.aistudio.pipeline.RunStep;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class Dtos {

    public record SessionSummary(String id, String title, String createdAt, String lastActivityAt) {}

    public record AskRequest(@NotBlank(message = "question must not be blank") String question) {}

    public record AskResponse(String sessionId, String runId, String answer, List<RunStep> steps) {}

    public record MessageDto(String id, String role, String text, String createdAt) {}

}
