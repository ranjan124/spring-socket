package com.shieldteq.trade.service;

import com.shieldteq.trade.client.StockClient;
import com.shieldteq.trade.client.UserClient;
import com.shieldteq.trade.dto.TradeRequest;
import com.shieldteq.trade.dto.TradeResponse;
import com.shieldteq.trade.dto.TradingStatus;
import com.shieldteq.trade.dto.TradingType;
import com.shieldteq.trade.dto.user.TransactionRequest;
import com.shieldteq.trade.dto.user.TransactionStatus;
import com.shieldteq.trade.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradingService {
    private final UserStockService userStockService;
    private final StockClient stockClient;
    private final UserClient userClient;

    public Mono<TradeResponse> trade(TradeRequest request) {
        TransactionRequest transactionRequest = EntityDtoMapper.toTransactionRequest(request, estimatePrice(request));
        Mono<TradeResponse> tradeResponseMono = TradingType.BUY.equals(request.type()) ? buyStock(request, transactionRequest) : sellStock(request, transactionRequest);
        return tradeResponseMono.defaultIfEmpty(EntityDtoMapper.toTradeResponse(request, TradingStatus.FAILED, 0));
    }

    private Mono<TradeResponse> buyStock(TradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return userClient.doTransaction(transactionRequest)
                .filter(response -> TransactionStatus.COMPLETED.equals(response.status()))
                .flatMap(response -> userStockService.buyStock(tradeRequest))
                .map(us -> EntityDtoMapper.toTradeResponse(tradeRequest, TradingStatus.COMPLETED, transactionRequest.amount()));
    }

    private Mono<TradeResponse> sellStock(TradeRequest tradeRequest, TransactionRequest transactionRequest) {
        return userStockService.sellStock(tradeRequest)
                .flatMap(us -> userClient.doTransaction(transactionRequest))
                .map(us -> EntityDtoMapper.toTradeResponse(tradeRequest, TradingStatus.COMPLETED, transactionRequest.amount()));
    }

    private int estimatePrice(TradeRequest request) {
        return request.quantity() * stockClient.getStockPrice(request.symbol());
    }
}
