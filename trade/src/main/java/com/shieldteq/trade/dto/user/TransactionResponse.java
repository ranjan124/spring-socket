package com.shieldteq.trade.dto.user;

import lombok.Builder;

@Builder
public record TransactionResponse(String userId,
                                  Integer amount,
                                  TransactionType type,
                                  TransactionStatus status) {
}
