package com.shieldteq.trade.dto.user;

import lombok.Builder;

@Builder
public record UserStockDto(String id,
                           String userId,
                           String symbol,
                           int quantity) {
}
