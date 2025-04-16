package com.shieldteq.socket;

import com.shieldteq.socket.dto.RequestDto;
import com.shieldteq.socket.dto.ResponseDto;
import com.shieldteq.socket.util.ObjectUtil;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionSetupTest {
    private RSocketClient client;

    @BeforeAll
    void setUp() {
        Mono<RSocket> rsocket = RSocketConnector.create()
                .setupPayload(DefaultPayload.create("client3"))
                .connect(TcpClientTransport.create("localhost", 8888))
                .doOnNext(r -> System.out.println("Connected"));
        client = RSocketClient.from(rsocket);
    }

    @Test
    public void persistentConnectionTest() {
        Payload payload = ObjectUtil.toPayload(new RequestDto(10));
        Flux<ResponseDto> responseFlux = client.requestStream(Mono.just(payload))
                .map(x -> ObjectUtil.fromPayload(x, ResponseDto.class))
                .doOnNext(System.out::println);
        StepVerifier.create(responseFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

}
