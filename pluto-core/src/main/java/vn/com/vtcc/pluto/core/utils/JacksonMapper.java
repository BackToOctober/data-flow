package vn.com.vtcc.pluto.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonMapper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String parseToString(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
