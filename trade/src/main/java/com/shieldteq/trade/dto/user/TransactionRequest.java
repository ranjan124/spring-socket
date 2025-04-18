package com.shieldteq.trade.dto.user;

import lombok.Builder;

@Builder
public record TransactionRequest(String userId,
                                 Integer amount,
                                 TransactionType type) {
}
