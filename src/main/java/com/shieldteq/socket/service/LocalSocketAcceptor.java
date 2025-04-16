package com.shieldteq.socket.service;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class LocalSocketAcceptor implements SocketAcceptor {
    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket rSocket) {
        log.info("Accepting Socket");
        System.out.println("Accepting Socket");
        if (isValidClient(setup.getDataUtf8())) {
            return Mono.just(new MathService());
        } else {
            return Mono.just(new FreeService());
        }
//        return Mono.fromCallable(MathService::new);
//        return Mono.fromCallable(() -> new BatchJobService(rSocket));
//        return Mono.fromCallable(FastProducerService::new);
    }

    boolean isValidClient(String client) {
        return "client1".equals(client) || "client2".equals(client);
    }
}

