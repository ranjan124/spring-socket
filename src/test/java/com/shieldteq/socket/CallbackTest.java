package com.shieldteq.socket;

import com.shieldteq.socket.dto.RequestDto;
import com.shieldteq.socket.service.CallbackService;
import com.shieldteq.socket.util.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CallbackTest {
    private RSocket rsocket;

    @BeforeAll
    void setUp() {
        this.rsocket = RSocketConnector
                .create()
                .acceptor(SocketAcceptor.with(new CallbackService()))
                .connect(TcpClientTransport.create("localhost", 8888))
                .block();
    }

    @Test
    public void callbackTest() throws InterruptedException {
        Payload payload = DefaultPayload.create(ObjectUtil.toPayload(new RequestDto(10)));
        Mono<Void> responseMono = rsocket.fireAndForget(payload);
        StepVerifier.create(responseMono)
                .verifyComplete();
        Thread.sleep(Duration.ofSeconds(10));
    }

}
