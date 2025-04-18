package com.shieldteq.user.dto;

import lombok.Builder;

@Builder
public record TransactionRequest(String userId,
                                 Integer amount,
                                 TransactionType type) {
}
