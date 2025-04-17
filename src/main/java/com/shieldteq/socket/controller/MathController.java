package com.shieldteq.socket.controller;

import com.shieldteq.socket.dto.ComputationRequestDto;
import com.shieldteq.socket.dto.ComputationResponseDto;
import com.shieldteq.socket.dto.GraphResponseDto;
import com.shieldteq.socket.service.MathService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class MathController {
    private final MathService mathService;

    @MessageMapping("math.service.print")
    public Mono<Void> print(Mono<ComputationRequestDto> requestMono) {
        return mathService.print(requestMono);
    }

    @MessageMapping("math.service.square")
    public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestMono) {
        return mathService.square(requestMono);
    }

    @MessageMapping("math.service.table")
    public Flux<ComputationResponseDto> findTable(Mono<ComputationRequestDto> requestMono) {
        return requestMono.flatMapMany(mathService::tableStream);
    }

    @MessageMapping("math.service.chart")
    public Flux<GraphResponseDto> findSquare(Flux<ComputationRequestDto> requestMono) {
        return mathService.graphStream(requestMono);
    }
}
