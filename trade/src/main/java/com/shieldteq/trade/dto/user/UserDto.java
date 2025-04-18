package com.shieldteq.trade.dto.user;

import lombok.Builder;

@Builder
public record UserDto(String id,
                      String name,
                      Integer balance) {
}
