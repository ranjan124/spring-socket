package com.shieldteq.user.service;

import com.shieldteq.user.dto.TransactionRequest;
import com.shieldteq.user.dto.TransactionResponse;
import com.shieldteq.user.dto.TransactionStatus;
import com.shieldteq.user.dto.TransactionType;
import com.shieldteq.user.entity.User;
import com.shieldteq.user.repository.UserRepository;
import com.shieldteq.user.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class UserTransactionService {
    private final UserRepository userRepository;

    public Mono<TransactionResponse> doTransaction(TransactionRequest request) {
        UnaryOperator<Mono<User>> transaction = request.type() == TransactionType.CREDIT ? credit(request) : debit(request);
        return userRepository.findById(request.userId())
                .transform(transaction)
                .flatMap(userRepository::save)
                .map(s -> EntityDtoMapper.toResponse(request, TransactionStatus.COMPLETED))
                .defaultIfEmpty(EntityDtoMapper.toResponse(request, TransactionStatus.FAILED));

    }

    UnaryOperator<Mono<User>> credit(TransactionRequest request) {
        return mono -> mono.map(u -> u.addBalance(request.amount()));
    }

    UnaryOperator<Mono<User>> debit(TransactionRequest request) {
        return mono -> mono.filter(u -> u.balance() >= request.amount()).map(u -> u.deductBalance(request.amount()));

    }
}
