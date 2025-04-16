package com.shieldteq.socket.dto;

import lombok.Builder;

@Builder
public record GraphResponseDto(int input, int output) {

    @Override
    public String toString() {
        String format = getFormat(output);
        return String.format(format, output, "x");
    }

    private String getFormat(int value) {
        return "%3s|%" + value + "s";
    }
}
