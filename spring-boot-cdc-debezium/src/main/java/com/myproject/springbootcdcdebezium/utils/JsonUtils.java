package com.myproject.springbootcdcdebezium.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 12:36 AM Mon 2/17/2025
 */
@UtilityClass
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LogUtils.info("Error while parsing json");
            LogUtils.error(e);
            return "";
        }
    }

    public static String compressLog(String key, String value) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(key);
        sb.append(value);
        sb.append("}");
        return sb.toString();
    }

}
