package com.shieldteq.user.util;

import com.shieldteq.user.dto.TransactionRequest;
import com.shieldteq.user.dto.TransactionResponse;
import com.shieldteq.user.dto.TransactionStatus;
import com.shieldteq.user.dto.UserDto;
import com.shieldteq.user.entity.User;

public final class EntityDtoMapper {
    private EntityDtoMapper() {
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.id())
                .balance(user.balance())
                .name(user.name()).build();
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.id())
                .balance(dto.balance())
                .name(dto.name()).build();
    }

    public static User copyEntity(String id, UserDto dto) {
        return User.builder()
                .id(id)
                .balance(dto.balance())
                .name(dto.name()).build();
    }

    public static TransactionResponse toResponse(TransactionRequest request, TransactionStatus status) {
        return TransactionResponse.builder()
                .userId(request.userId())
                .amount(request.amount())
                .type(request.type())
                .status(status).build();

    }
}
