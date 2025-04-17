package com.shieldteq.socket.controller;

import com.shieldteq.socket.service.MathClientManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class ConnectionHandler {
    private final MathClientManager mathClientManager;

    //    @ConnectMapping
//    public Mono<Void> handleConnection(ClientConnectionRequest request, RSocketRequester requester) {
//        System.out.println("Connection established : " + request);
//        return "secret".equals(request.secret()) ? Mono.empty() : Mono.fromRunnable(() -> requester.rsocketClient().dispose());
//    }
    @ConnectMapping
    public Mono<Void> handleConnection(RSocketRequester requester) {
        System.out.println("Connection established");
        return Mono.fromRunnable(() -> mathClientManager.addClient(requester));
    }
}
