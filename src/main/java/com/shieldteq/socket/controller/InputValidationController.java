package com.shieldteq.socket.controller;

import com.shieldteq.socket.dto.Response;
import com.shieldteq.socket.error.ErrorEvent;
import com.shieldteq.socket.error.StatusCode;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("math.validation")
public class InputValidationController {
    // Error not handled properly
//    @MessageMapping("double.{input}")
//    public Mono<Integer> validate(@DestinationVariable int input) {
//        if (input < 31) {
//            return Mono.just(input * 2);
//        }else return Mono.error(new IllegalArgumentException("Input must not be greater than 30"));
//    }
    @MessageMapping("double.{input}")
    public Mono<Integer> validate(@DestinationVariable int input) {
        return Mono.just(input)
                .filter(i -> i < 31)
                .map(i -> i * 2)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Input must not be greater than 30")));
    }

    @MessageMapping("double.response.{input}")
    public Mono<Response<Integer>> validateResponse(@DestinationVariable int input) {
        return Mono.just(input)
                .filter(i -> i < 31)
                .map(i -> i * 2)
                .map(Response::with)
                .defaultIfEmpty(Response.with(new ErrorEvent(StatusCode.EC001)));
    }

    @MessageExceptionHandler
    public Mono<Integer> handleException(Exception e) {
        System.out.println(e.getMessage());
        return Mono.just(-1);
    }

}
