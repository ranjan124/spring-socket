package com.shieldteq.trade.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record UserStock(@Id String id,
                        String userId,
                        String symbol,
                        int quantity) {
    public UserStock addQuantity(int quantity) {
        return UserStock.builder()
                .id(id)
                .userId(userId)
                .symbol(symbol)
                .quantity(this.quantity + quantity)
                .build();
    }
    public UserStock deleteQuantity(int quantity) {
        return UserStock.builder()
                .id(id)
                .userId(userId)
                .symbol(symbol)
                .quantity(this.quantity - quantity)
                .build();
    }

}
