package com.shieldteq.stock;

import com.shieldteq.stock.dto.StockPriceDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StockApplicationTests {
    private RSocketRequester requester;
    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        this.requester = this.builder.transport(TcpClientTransport.create("localhost", 7070));
    }

    @Test
    public void testStockPrice() {
        this.requester.route("stock.price")
                .retrieveFlux(StockPriceDto.class)
                .doOnNext(System.out::println)
                .take(12)
                .as(StepVerifier::create)
                .expectNextCount(12)
                .verifyComplete();
    }
}
