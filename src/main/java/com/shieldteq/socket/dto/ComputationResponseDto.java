package com.shieldteq.socket.dto;

import lombok.Builder;

@Builder
public record ComputationResponseDto(int input, int output) {
}
