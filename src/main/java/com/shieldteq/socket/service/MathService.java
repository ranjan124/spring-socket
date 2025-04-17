package com.shieldteq.socket.service;

import com.shieldteq.socket.dto.ComputationRequestDto;
import com.shieldteq.socket.dto.ComputationResponseDto;
import com.shieldteq.socket.dto.GraphResponseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MathService {

    public Mono<Void> print(Mono<ComputationRequestDto> requestMono) {
        return requestMono
                .doOnNext(r -> System.out.println("Received: " + r))
                .then();

    }

    public Mono<ComputationResponseDto> square(Mono<ComputationRequestDto> requestMono) {
        return requestMono
                .map(ComputationRequestDto::input)
                .map(i -> new ComputationResponseDto(i, i * i));
    }

    public Flux<ComputationResponseDto> tableStream(ComputationRequestDto request) {
        return Flux.range(1, 10)
                .map(i -> ComputationResponseDto.builder().input(request.input()).output(i * request.input()).build());
    }

    public Flux<GraphResponseDto> graphStream(Flux<ComputationRequestDto> requestFlux) {
        return requestFlux
                .map(ComputationRequestDto::input)
                .map(r -> GraphResponseDto.builder().input(r).output((r * r) + 1).build());
    }
}
