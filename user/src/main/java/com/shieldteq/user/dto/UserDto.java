package com.shieldteq.user.dto;

import lombok.Builder;

@Builder
public record UserDto(String id,
                      String name,
                      Integer balance) {
}
