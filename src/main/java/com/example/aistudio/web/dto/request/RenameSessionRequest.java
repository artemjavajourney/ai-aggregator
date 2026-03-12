package com.example.aistudio.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RenameSessionRequest(
        @NotBlank(message = "title must not be blank")
        @Size(max = 48, message = "title is too long")
        String title
) {
}
