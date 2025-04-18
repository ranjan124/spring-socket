package com.shieldteq.trade.dto;

public record TradeRequest(String userId,
                           String symbol,
                           int quantity,
                           TradingType type) {
}
