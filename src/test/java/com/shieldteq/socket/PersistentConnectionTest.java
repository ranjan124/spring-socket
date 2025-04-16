package com.shieldteq.socket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
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
public class PersistentConnectionTest {
    private RSocketClient client;

    @BeforeAll
    void setUp() {
        Mono<RSocket> rsocket = RSocketConnector
                .create()
                .connect(TcpClientTransport.create("localhost", 8888))
                .doOnNext(r -> System.out.println("Connected"));
        client = RSocketClient.from(rsocket);
    }

    @Test
    public void persistentConnectionTest() throws InterruptedException {
        Flux<String> responseFlux = client.requestStream(Mono.just(DefaultPayload.create("")))
                .map(Payload::getDataUtf8)
                .delayElements(Duration.ofMillis(300))
                .take(10)
                .doOnNext(System.out::println);
        StepVerifier.create(responseFlux)
                .expectNextCount(10)
                .verifyComplete();

        Thread.sleep(Duration.ofSeconds(10));
        System.out.println("Waking after 10 seconds");
        Flux<String> response2Flux = client.requestStream(Mono.just(DefaultPayload.create("")))
                .map(Payload::getDataUtf8)
                .delayElements(Duration.ofMillis(300))
                .take(10)
                .doOnNext(System.out::println);
        StepVerifier.create(response2Flux)
                .expectNextCount(10)
                .verifyComplete();
    }

}
