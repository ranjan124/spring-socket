package com.shieldteq.stock.service;

import com.shieldteq.stock.dto.StockPriceDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class StockService {
    private static final Stock AMAZON = Stock.builder().code("AMAZON").price(100).volatility(10).build();
    private static final Stock MICROSOFT = Stock.builder().code("MICROSOFT").price(300).volatility(30).build();
    private static final Stock GOOGLE = Stock.builder().code("GOOGLE").price(600).volatility(80).build();

    public Flux<StockPriceDto> getStockPrice() {
        return Flux.interval(Duration.ofSeconds(2))
                .flatMap(i -> Flux.just(AMAZON, MICROSOFT, GOOGLE))
                .map(Stock::getUpdatedStock)
                .map(s -> new StockPriceDto(s.code(), s.price()));
    }
}
