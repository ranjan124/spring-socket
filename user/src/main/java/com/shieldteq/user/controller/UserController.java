package com.shieldteq.user.controller;

import com.shieldteq.user.dto.UserDto;
import com.shieldteq.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MessageMapping("get.all")
    public Flux<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @MessageMapping("get.{id}")
    public Mono<UserDto> getUserById(@DestinationVariable String id) {
        return userService.getUserById(id);
    }

    @MessageMapping("create")
    public Mono<UserDto> createUser(Mono<UserDto> userDto) {
        return userService.createUser(userDto);
    }

    @MessageMapping("update.{id}")
    public Mono<UserDto> updateUser(@DestinationVariable String id, Mono<UserDto> userDto) {
        return userService.updateUser(id, userDto);
    }

    @MessageMapping("delete.{id}")
    public Mono<Void> deleteUser(@DestinationVariable String id) {
        return userService.deleteUser(id);
    }
}
