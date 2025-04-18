package com.shieldteq.trade.service;

import com.shieldteq.trade.dto.TradeRequest;
import com.shieldteq.trade.dto.user.UserStockDto;
import com.shieldteq.trade.entity.UserStock;
import com.shieldteq.trade.repository.UserStockRepository;
import com.shieldteq.trade.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserStockService {
    private final UserStockRepository userStockRepository;

    public Mono<UserStock> buyStock(TradeRequest request) {
        return userStockRepository.findByUserIdAndSymbol(request.userId(), request.symbol())
                .defaultIfEmpty(EntityDtoMapper.toUserStock(request))
                .map(stock -> stock.addQuantity(request.quantity()))
                .flatMap(userStockRepository::save);
    }

    public Mono<UserStock> sellStock(TradeRequest request) {
        return userStockRepository.findByUserIdAndSymbol(request.userId(), request.symbol())
                .filter(stock -> stock.quantity() >= request.quantity())
                .map(stock -> stock.deleteQuantity(request.quantity()))
                .flatMap(userStockRepository::save);
    }

    public Flux<UserStockDto> getUserStocks(String userId) {
        return userStockRepository.findByUserId(userId)
                .map(EntityDtoMapper::toUserStockDto);
    }

}
