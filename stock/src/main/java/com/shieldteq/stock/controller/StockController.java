package com.shieldteq.stock.controller;

import com.shieldteq.stock.dto.StockPriceDto;
import com.shieldteq.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @MessageMapping("stock.price")
    public Flux<StockPriceDto> getStockPrice() {
        return stockService.getStockPrice();
    }
}
