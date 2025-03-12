package com.myproject.springbootoauth2.utils;

import java.util.Map;
import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 11:47 AM Wed 3/12/2025
 */
@UtilityClass
public class OAuth2AttributeUtils {

	public static boolean isAttributeNested(String attributeKey) {
		String[] keys = attributeKey.split("\\.");
		return keys.length > 1;
	}

	public static String getAttribute(Map<String, Object> attributes, String attributeKey) {
		try {
			if (!isAttributeNested(attributeKey)) {
				return attributes.get(attributeKey).toString();
			} else {
				String[] keys = attributeKey.split("\\.");
				Map<String, Object> subMap = attributes;

				for (int i = 0; i < keys.length - 1; i++) {
					Object value = subMap.get(keys[i]);

					if (!(value instanceof Map)) {
						return null;
					}

					subMap = (Map<String, Object>) value;
				}

				Object finalValue = subMap.get(keys[keys.length - 1]);
				return finalValue != null ? finalValue.toString() : null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

}
