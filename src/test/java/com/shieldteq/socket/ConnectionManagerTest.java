package com.shieldteq.socket;

import com.shieldteq.socket.dto.ComputationRequestDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
})
class ConnectionManagerTest {
    @Autowired
    private RSocketRequester.Builder builder;

    @Test
    public void connectionSetupTest() throws InterruptedException {
        RSocketRequester requester1 = builder
                .transport(TcpClientTransport.create("localhost", 6565));
        RSocketRequester requester2 = builder
                .transport(TcpClientTransport.create("localhost", 6565));
        requester1.route("math.service.print").data(ComputationRequestDto.builder().input(5).build()).send().subscribe();
        requester2.route("math.service.print").data(ComputationRequestDto.builder().input(5).build()).send().subscribe();

        Thread.sleep(Duration.ofSeconds(10));
    }
}
