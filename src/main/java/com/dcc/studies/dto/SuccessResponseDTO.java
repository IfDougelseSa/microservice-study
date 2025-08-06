package com.dcc.studies.dto;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class SuccessResponseDTO<T> {

    private final String statusCode;

    private final String statusMsg;

    private final LocalDateTime timestamp;

    private final T data;

    public SuccessResponseDTO(String statusCode, String statusMsg, T data) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}