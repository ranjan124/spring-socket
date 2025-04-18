package com.shieldteq.trade.config;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class RSocketClientConfig {
    @Bean
    @Order(1)
    public RSocketConnectorConfigurer connectorConfigurer() {
        return c -> c.reconnect(retryStrategy());
    }

    private Retry retryStrategy() {
        return Retry.fixedDelay(3, Duration.ofSeconds(2)).doBeforeRetry(i -> System.out.println("Retrying : " + i.totalRetriesInARow()));
    }

    @Bean
    @Order(2)
    @Qualifier("userClientRequester")
    public RSocketRequester userClientRequester(RSocketRequester.Builder builder,
                                      RSocketConnectorConfigurer connectorConfigurer,
                                      @Value("${user.service.host}") String host,
                                      @Value("${user.service.port}") int port) {
        return builder.rsocketConnector(connectorConfigurer).transport(TcpClientTransport.create(host, port));
    }

    @Bean
    @Order(2)
    @Qualifier("stockClientRequester")
    public RSocketRequester stockClientRequester(RSocketRequester.Builder builder,
                                      RSocketConnectorConfigurer connectorConfigurer,
                                      @Value("${stock.service.host}") String host,
                                      @Value("${stock.service.port}") int port) {
        return builder.rsocketConnector(connectorConfigurer).transport(TcpClientTransport.create(host, port));
    }
}
