package com.shieldteq.socket.dto;

import lombok.Builder;

@Builder
public record ResponseDto(int input, int output) {
}
