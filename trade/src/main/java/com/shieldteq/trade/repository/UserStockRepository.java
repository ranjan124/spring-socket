package com.shieldteq.trade.repository;

import com.shieldteq.trade.entity.UserStock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserStockRepository extends ReactiveMongoRepository<UserStock, String> {
    Mono<UserStock> findByUserIdAndSymbol(String userId, String symbol);
    Flux<UserStock> findByUserId(String userId);
}
