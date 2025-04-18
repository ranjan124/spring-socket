package com.shieldteq.stock.service;

import lombok.Builder;

import java.util.concurrent.ThreadLocalRandom;

@Builder
public record Stock(int price,
                    String code,
                    int volatility) {
    private Stock updatePrice() {
        int random = ThreadLocalRandom.current().nextInt(-1 * volatility, volatility + 1);
        int newPrice = random + price;
        newPrice = Math.max(newPrice, 0);
        return new Stock(newPrice, code, volatility);
    }

    public Stock getUpdatedStock() {
        return updatePrice();
    }
}
