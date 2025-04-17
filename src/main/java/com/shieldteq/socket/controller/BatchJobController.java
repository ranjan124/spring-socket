package com.shieldteq.socket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class BatchJobController {
    @MessageMapping("batch.job.request")
    public Mono<Void> submitJob(Mono<Integer> inputMono, RSocketRequester requester) {
        processJob(inputMono, requester);
        return Mono.empty();
    }

    private void processJob(Mono<Integer> input, RSocketRequester requester) {
        input.map(i -> (i * i) + 1)
                .flatMap(i -> requester.route("batch.job.response").data(i).send())
                .subscribe();
    }
}
