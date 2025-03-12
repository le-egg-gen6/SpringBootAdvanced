package com.myproject.springbootoauth2.utils;

import java.util.Iterator;
import java.util.List;
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
				List<String> keys = List.of(attributeKey.split("\\."));
				Iterator<String> iterator = keys.iterator();
				Map<String, Object> subMap = attributes;
				while (iterator.hasNext()) {
					String key = iterator.next();
					if (iterator.hasNext()) {
						subMap = (Map<String, Object>) subMap.get(key);
					} else {
						return subMap.get(key).toString();
					}
				}
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

}
