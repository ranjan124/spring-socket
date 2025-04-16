package com.shieldteq.socket.service;

import com.shieldteq.socket.dto.ResponseDto;
import com.shieldteq.socket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import reactor.core.publisher.Mono;

public class CallbackService implements RSocket {
    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        System.out.println("Received response : "+ ObjectUtil.fromPayload(payload, ResponseDto.class));
        return Mono.empty();
    }
}
