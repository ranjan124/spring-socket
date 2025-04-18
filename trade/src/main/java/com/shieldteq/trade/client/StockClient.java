package com.shieldteq.trade.client;

import com.shieldteq.trade.dto.stock.StockPriceDto;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockClient {
    private final RSocketRequester stockClientRequester;
    private static final Map<String, Integer> stockPrices = Collections.synchronizedMap(new HashMap<>());
    @Getter
    private Flux<StockPriceDto> stockStream;

    @PostConstruct
    private void initialize() {
        stockStream = stockClientRequester.route("stock.price")
                .retrieveFlux(StockPriceDto.class)
                .doOnNext(s -> stockPrices.put(s.code(), s.price()))
                .retryWhen(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(2)))
                .publish()
                .autoConnect();
    }

    public int getStockPrice(String code) {
        log.info("Getting stock price for {}", code);
        return stockPrices.getOrDefault(code, 0);
    }

}
