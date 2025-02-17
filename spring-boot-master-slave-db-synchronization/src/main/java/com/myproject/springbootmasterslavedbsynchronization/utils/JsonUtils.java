package com.myproject.springbootmasterslavedbsynchronization.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 12:26 AM Tue 2/18/2025
 */
@UtilityClass
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            LogUtils.info("Error while parsing object to json");
            LogUtils.error(e);
            return "";
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            LogUtils.info("Error while parsing json to object");
            LogUtils.error(e);
            return null;
        }
    }

}
