package com.shieldteq.user.controller;

import com.shieldteq.user.dto.TransactionRequest;
import com.shieldteq.user.dto.TransactionResponse;
import com.shieldteq.user.service.UserTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("user")
@RequiredArgsConstructor
public class UserTransactionController {
    private final UserTransactionService userTransactionService;

    @MessageMapping("transaction")
    public Mono<TransactionResponse> doTransaction(Mono<TransactionRequest> request) {
        return request.flatMap(userTransactionService::doTransaction);
    }
}
