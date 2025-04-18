package com.shieldteq.user.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;

@Builder
public record User(@Id String id,
                   String name,
                   Integer balance) {
    public User addBalance(Integer amount) {
        return User.builder()
                .id(id)
                .name(name)
                .balance(balance + amount)
                .build();
    }

    public User deductBalance(Integer amount) {
        return User.builder()
                .id(id)
                .name(name)
                .balance(balance - amount)
                .build();
    }
}
