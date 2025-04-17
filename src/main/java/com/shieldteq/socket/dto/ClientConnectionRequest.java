package com.shieldteq.socket.dto;

import lombok.Builder;

@Builder
public record ClientConnectionRequest(String clientId,
                                      String secret) {
}
