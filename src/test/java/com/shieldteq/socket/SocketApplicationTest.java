package com.shieldteq.socket;

import com.shieldteq.socket.dto.ComputationRequestDto;
import com.shieldteq.socket.dto.ComputationResponseDto;
import com.shieldteq.socket.dto.GraphResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SocketApplicationTest {
    private RSocketRequester requester;
    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        requester = builder.transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void fireAndForget() {
        requester.route("math.service.print")
                .data(ComputationRequestDto.builder().input(5).build())
                .send()
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @Test
    public void requestResponse() {
        requester.route("math.service.square")
                .data(ComputationRequestDto.builder().input(5).build())
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestStream() {
        requester.route("math.service.table")
                .data(ComputationRequestDto.builder().input(5).build())
                .retrieveFlux(ComputationResponseDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void requestChannel() {
        Flux<ComputationRequestDto> dtoFlux = Flux.range(-10, 21).map(i -> ComputationRequestDto.builder().input(i).build());
        requester.route("math.service.chart")
                .data(dtoFlux)
                .retrieveFlux(GraphResponseDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(21)
                .verifyComplete();
    }
}
