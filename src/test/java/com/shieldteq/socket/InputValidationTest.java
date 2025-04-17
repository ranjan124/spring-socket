package com.shieldteq.socket;

import com.shieldteq.socket.dto.Response;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InputValidationTest {
    private RSocketRequester requester;
    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        requester = builder.transport(TcpClientTransport.create("localhost", 6565));
    }

    @Test
    public void validationTest() {
        requester.route("math.validation.double.31")
                .retrieveMono(Integer.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }
    @Test
    public void validationResponseTest() {
        requester.route("math.validation.double.response.30")
                .retrieveMono(new ParameterizedTypeReference<Response<Integer>>() {
                })
                .doOnNext(r -> {
                    if(r.hasError()){
                        System.out.println(r.getError().getStatusCode().getMessage());
                    }else {
                        System.out.println(r.getSuccess());
                    }
                })
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

}
