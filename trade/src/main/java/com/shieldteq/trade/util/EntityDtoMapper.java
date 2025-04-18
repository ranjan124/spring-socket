package com.shieldteq.trade.util;

import com.shieldteq.trade.dto.TradeRequest;
import com.shieldteq.trade.dto.TradeResponse;
import com.shieldteq.trade.dto.TradingStatus;
import com.shieldteq.trade.dto.TradingType;
import com.shieldteq.trade.dto.user.TransactionRequest;
import com.shieldteq.trade.dto.user.TransactionType;
import com.shieldteq.trade.dto.user.UserStockDto;
import com.shieldteq.trade.entity.UserStock;

public abstract class EntityDtoMapper {
    private EntityDtoMapper() {
    }

    public static UserStock toUserStock(TradeRequest request) {
        return UserStock.builder()
                .userId(request.userId())
                .symbol(request.symbol())
                .quantity(0)
                .build();
    }

    public static TransactionRequest toTransactionRequest(TradeRequest request, int amount) {
        TransactionType transactionType = TradingType.BUY.equals(request.type()) ? TransactionType.DEBIT : TransactionType.CREDIT;
        return TransactionRequest.builder()
                .amount(amount)
                .type(transactionType)
                .userId(request.userId())
                .build();
    }

    public static TradeResponse toTradeResponse(TradeRequest request, TradingStatus status, int price) {
        return TradeResponse.builder()
                .price(price)
                .status(status)
                .type(request.type())
                .symbol(request.symbol())
                .quantity(request.quantity())
                .userId(request.userId())
                .build();
    }

    public static UserStockDto toUserStockDto(UserStock stock) {
        return UserStockDto.builder()
                .id(stock.id())
                .symbol(stock.symbol())
                .quantity(stock.quantity())
                .userId(stock.userId())
                .build();
    }
}
