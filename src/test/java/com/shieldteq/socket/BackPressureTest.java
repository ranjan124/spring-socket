package com.shieldteq.socket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BackPressureTest {
    private RSocket rsocket;

    @BeforeAll
    void setUp() {
        this.rsocket = RSocketConnector
                .create()
                .connect(TcpClientTransport.create("localhost", 8888))
                .block();
    }

    @Test
    public void backPressureTest() {
        Flux<String> responseFlux = rsocket.requestStream(DefaultPayload.create(""))
                .map(Payload::getDataUtf8)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(System.out::println);
        StepVerifier.create(responseFlux)
                .expectNextCount(1000)
                .verifyComplete();
    }

}
