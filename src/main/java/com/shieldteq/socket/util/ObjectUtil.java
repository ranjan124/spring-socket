package com.shieldteq.socket.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rsocket.Payload;
import io.rsocket.util.DefaultPayload;

import java.io.IOException;

public final class ObjectUtil {
    private ObjectUtil() {
    }

    public static Payload toPayload(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(obj);
            return DefaultPayload.create(bytes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromPayload(Payload payload, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(payload.getData().array(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
