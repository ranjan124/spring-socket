package com.shieldteq.trade.controller;

import com.shieldteq.trade.client.UserClient;
import com.shieldteq.trade.dto.user.UserDto;
import com.shieldteq.trade.dto.user.UserStockDto;
import com.shieldteq.trade.service.UserStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserClient userClient;
    private final UserStockService userStockService;

    @GetMapping("/all")
    public Flux<UserDto> getAllUsers() {
        return userClient.allUsers();
    }

    @GetMapping("/{userId}/stocks")
    public Flux<UserStockDto> getUserStocks(@PathVariable String userId) {
        return userStockService.getUserStocks(userId);
    }

}
