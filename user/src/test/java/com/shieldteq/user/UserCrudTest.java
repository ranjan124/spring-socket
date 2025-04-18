package com.shieldteq.user;

import com.shieldteq.user.dto.UserDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserCrudTest {
    private RSocketRequester requester;
    @Autowired
    private RSocketRequester.Builder builder;

    @BeforeAll
    public void setup() {
        requester = builder.transport(TcpClientTransport.create("localhost", 7071));
    }

    @Test
    void allUserTest() {
        requester.route("user.get.all")
                .retrieveFlux(UserDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void singleUserTest() {
        requester.route("user.get.test2")
                .retrieveMono(UserDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void singleUserCreateTest() {
        UserDto dto = UserDto.builder().name("test3").balance(100).build();
        requester.route("user.create")
                .data(dto)
                .retrieveMono(UserDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void singleUserUpdateTest() {
        UserDto dto = UserDto.builder().name("test3").balance(200).build();
        requester.route("user.update.{id}", "test3")
                .data(dto)
                .retrieveMono(UserDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    @Disabled
    void singleUserDeleteTest() {
        requester.route("user.delete.{id}", "test3")
                .send()
                .as(StepVerifier::create)
                .verifyComplete();


        requester.route("user.get.all")
                .retrieveFlux(UserDto.class)
                .doOnNext(System.out::println)
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

}
