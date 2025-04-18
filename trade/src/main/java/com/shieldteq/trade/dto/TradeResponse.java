package com.shieldteq.trade.dto;

import lombok.Builder;

@Builder
public record TradeResponse(String userId,
                            String symbol,
                            int quantity,
                            TradingType type,
                            TradingStatus status,
                            int price) {
}
