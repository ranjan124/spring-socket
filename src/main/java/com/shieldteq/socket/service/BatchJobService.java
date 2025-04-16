package com.shieldteq.socket.service;

import com.shieldteq.socket.dto.RequestDto;
import com.shieldteq.socket.dto.ResponseDto;
import com.shieldteq.socket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequiredArgsConstructor
public class BatchJobService implements RSocket {
    private final RSocket rsocket;

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RequestDto requestDto = ObjectUtil.fromPayload(payload, RequestDto.class);
        System.out.println("Received request for batch job " + requestDto);
        Mono.just(requestDto)
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .flatMap(this::findCube)
                .subscribe();
        return Mono.empty();
    }

    public Mono<Void> findCube(RequestDto requestDto) {
        int input = requestDto.input();
        int output = input * input * input;
        System.out.println("Cube of " + input + " is " + output);
        ResponseDto responseDto = ResponseDto.builder().input(input).output(output).build();
        Payload payload = ObjectUtil.toPayload(responseDto);
        return rsocket.fireAndForget(payload);
    }
}
