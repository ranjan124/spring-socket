package com.shieldteq.socket;

import com.shieldteq.socket.dto.ClientConnectionRequest;
import com.shieldteq.socket.dto.ComputationRequestDto;
import com.shieldteq.socket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnectionSetupTest {
    private RSocketRequester requester;
    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        ClientConnectionRequest request = ClientConnectionRequest.builder().clientId("clientId").secret("secret").build();
        requester = builder
                .setupData(request)
                .transport(TcpClientTransport.create("localhost", 6565));
    }

    @RepeatedTest(3)
    public void connectionSetupTest() {
        requester.route("math.service.square")
                .data(ComputationRequestDto.builder().input(ThreadLocalRandom.current().nextInt(1, 50)).build())
                .retrieveMono(ComputationResponseDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
}
