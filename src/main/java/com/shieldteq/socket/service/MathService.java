package com.shieldteq.socket.service;

import com.shieldteq.socket.dto.GraphResponseDto;
import com.shieldteq.socket.dto.RequestDto;
import com.shieldteq.socket.dto.ResponseDto;
import com.shieldteq.socket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class MathService implements RSocket {
    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        log.info("receiving {}", ObjectUtil.fromPayload(payload, RequestDto.class));
        System.out.println("receiving " + ObjectUtil.fromPayload(payload, RequestDto.class));
        return Mono.empty();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        return Mono.fromSupplier(() -> {
            RequestDto requestDto = ObjectUtil.fromPayload(payload, RequestDto.class);
            log.info("receiving {}", requestDto);
            System.out.println("receiving " + requestDto);
            ResponseDto responseDto = ResponseDto.builder().input(requestDto.input()).output(requestDto.input() * requestDto.input()).build();
            return ObjectUtil.toPayload(responseDto);
        });
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RequestDto requestDto = ObjectUtil.fromPayload(payload, RequestDto.class);
        return Flux.range(1, 10)
                .map(i -> i * requestDto.input())
                .map(i -> ResponseDto.builder().input(requestDto.input()).output(i).build())
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println)
                .map(ObjectUtil::toPayload);
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.from(payloads)
                .map(p -> ObjectUtil.fromPayload(p, RequestDto.class))
                .map(RequestDto::input)
                .map(i -> GraphResponseDto.builder().input(i).output((i * i) + 1).build())
                .map(ObjectUtil::toPayload);
    }
}
