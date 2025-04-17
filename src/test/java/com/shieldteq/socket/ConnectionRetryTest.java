package com.shieldteq.socket;

import com.shieldteq.socket.dto.ComputationRequestDto;
import com.shieldteq.socket.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
})
class ConnectionRetryTest {
    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void connectionSetupTest() throws InterruptedException {
        RSocketRequester requester1 = builder
                .rsocketConnector(c -> c.reconnect(Retry.fixedDelay(50, Duration.ofSeconds(2))
                        .doBeforeRetry(r -> System.out.println("retrying connection: " + r.totalRetriesInARow()))))
                .transport(TcpClientTransport.create("localhost", 6565));
        for (int i = 0; i < 50; i++) {
            requester1.route("math.service.square")
                    .data(ComputationRequestDto.builder().input(5).build())
                    .retrieveMono(ComputationResponseDto.class)
                    .doOnNext(System.out::println)
                    .as(StepVerifier::create)
                    .expectNextCount(1)
                    .verifyComplete();
            Thread.sleep(Duration.ofSeconds(2));
        }

    }
}
