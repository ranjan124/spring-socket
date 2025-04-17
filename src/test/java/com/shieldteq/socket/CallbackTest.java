package com.shieldteq.socket;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CallbackTest {
    private RSocketRequester requester;
    @Autowired
    private RSocketRequester.Builder builder;
    @Autowired
    private RSocketMessageHandler messageHandler;

    @BeforeAll
    public void setup() {
        requester = builder
                .rsocketConnector(c -> c.acceptor(messageHandler.responder()))
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void callbackTest() throws InterruptedException {
        requester.route("batch.job.request")
                .data(10)
                .send()
                .as(StepVerifier::create)
                .verifyComplete();

        Thread.sleep(Duration.ofSeconds(12));
    }
}
