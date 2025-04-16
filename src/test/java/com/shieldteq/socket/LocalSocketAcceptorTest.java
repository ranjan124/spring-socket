package com.shieldteq.socket;

import com.shieldteq.socket.dto.GraphResponseDto;
import com.shieldteq.socket.dto.RequestDto;
import com.shieldteq.socket.dto.ResponseDto;
import com.shieldteq.socket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocalSocketAcceptorTest {
    private RSocket rsocket;

    @BeforeAll
    void setUp() {
        this.rsocket = RSocketConnector
                .create()
                .connect(TcpClientTransport.create("localhost", 8888))
                .block();
    }

    @Test
    public void fireAndForgetTest() {
        Payload payload = DefaultPayload.create(ObjectUtil.toPayload(new RequestDto(10)));
        Mono<Void> responseMono = rsocket.fireAndForget(payload);
        StepVerifier.create(responseMono)
                .verifyComplete();
    }

    @Test
    public void requestAndResponseTest() {
        Payload payload = DefaultPayload.create(ObjectUtil.toPayload(new RequestDto(10)));
        Mono<ResponseDto> responseMono = rsocket.requestResponse(payload)
                .map(p -> ObjectUtil.fromPayload(p, ResponseDto.class))
                .doOnNext(System.out::println);
        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void requestStreamTest() {
        Payload payload = DefaultPayload.create(ObjectUtil.toPayload(new RequestDto(10)));
        Flux<ResponseDto> responseFlux = rsocket.requestStream(payload)
                .map(p -> ObjectUtil.fromPayload(p, ResponseDto.class))
                .doOnNext(System.out::println)
                .take(4);
        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void requestChannelTest() {
        Flux<Payload> payload = Flux.range(-10, 21)
                .delayElements(Duration.ofMillis(500))
                .map(i -> ObjectUtil.toPayload(new RequestDto(i)));

        Flux<GraphResponseDto> response = rsocket.requestChannel(payload)
                .map(p -> ObjectUtil.fromPayload(p, GraphResponseDto.class))
                .doOnNext(System.out::println);
        StepVerifier.create(response)
                .expectNextCount(21)
                .verifyComplete();
    }

}
