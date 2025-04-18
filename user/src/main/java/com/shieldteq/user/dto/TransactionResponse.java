package com.shieldteq.user.dto;

import lombok.Builder;

@Builder
public record TransactionResponse(String userId,
                                  Integer amount,
                                  TransactionType type,
                                  TransactionStatus status) {
}
