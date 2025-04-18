package com.shieldteq.trade.client;

import com.shieldteq.trade.dto.user.TransactionRequest;
import com.shieldteq.trade.dto.user.TransactionResponse;
import com.shieldteq.trade.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserClient {
    private final RSocketRequester userClientRequester;

    public Mono<TransactionResponse> doTransaction(TransactionRequest request) {
        return userClientRequester.route("user.transaction")
                .data(request)
                .retrieveMono(TransactionResponse.class)
                .doOnNext(System.out::println);
    }

    public Flux<UserDto> allUsers() {
        return userClientRequester.route("user.get.all")
                .retrieveFlux(UserDto.class);
    }

}
