package com.dcc.studies.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ContentCreateDTO {
    @NotBlank(message = "Title cannot be null.")
    private String title;

    @NotBlank(message = "Content body cannot be empty.")
    private String body;
}