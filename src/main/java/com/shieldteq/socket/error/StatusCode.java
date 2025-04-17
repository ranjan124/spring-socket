package com.shieldteq.socket.error;

import lombok.Getter;

@Getter
public enum StatusCode {
    EC001("Given number is not in range"),
    EC002("Usage limit exceeded"),
    EC003("Unknown Error");

    private final String message;

    StatusCode(String message) {
        this.message = message;
    }

}
