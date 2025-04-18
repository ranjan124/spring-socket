package com.shieldteq.trade.controller;

import com.shieldteq.trade.client.StockClient;
import com.shieldteq.trade.dto.TradeRequest;
import com.shieldteq.trade.dto.TradeResponse;
import com.shieldteq.trade.dto.stock.StockPriceDto;
import com.shieldteq.trade.service.TradingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {
    private final TradingService tradingService;
    private final StockClient stockClient;

    @GetMapping(value = "/stockPrice", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockPriceDto> getStockPrice() {
        return stockClient.getStockStream();
    }

    @PostMapping
    public Mono<ResponseEntity<TradeResponse>> trade(@RequestBody Mono<TradeRequest> tradeRequest) {

        return tradeRequest
                .filter(tr -> tr.quantity() > 0)
                .flatMap(tradingService::trade)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }


}
