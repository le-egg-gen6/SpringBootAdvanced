package com.myproject.springbootpayment.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 6:12 PM Wed 4/23/2025
 */
@UtilityClass
public class JsonUtils {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static String toJson(Object obj) {
		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (Exception ex) {
			return null;
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (Exception ex) {
			return null;
		}
	}
}
