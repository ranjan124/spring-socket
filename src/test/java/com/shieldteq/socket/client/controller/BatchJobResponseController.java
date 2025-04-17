package com.shieldteq.socket.client.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class BatchJobResponseController {
    @MessageMapping("batch.job.response")
    public Mono<Void> processResponse(Mono<Integer> response) {
        return response.doOnNext(System.out::println).then();
    }
}
