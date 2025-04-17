package com.shieldteq.socket.error;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class ErrorEvent {
    private final StatusCode statusCode;
    private LocalDate date = LocalDate.now();

    public ErrorEvent(StatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
