package com.shieldteq.socket.dto;

import com.shieldteq.socket.error.ErrorEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class Response<T> {
    ErrorEvent error;
    T success;

    public Response(ErrorEvent error) {
        this.error = error;
    }

    public Response(T success) {
        this.success = success;
    }

    public boolean hasError() {
        return Objects.nonNull(error);
    }

    public static <T> Response<T> with(T t) {
        return new Response<>(t);
    }

    public static <T> Response<T> with(ErrorEvent error) {
        return new Response<>(error);
    }
}
