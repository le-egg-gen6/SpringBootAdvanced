package com.myproject.springbootoauth2.shared;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * @author nguyenle
 * @since 10:36 AM Wed 3/12/2025
 */
@Getter
public enum AuthProvider {
	LOCAL("local"),
	GOOGLE("google"),
	FACEBOOK("facebook"),
	GITHUB("github")
	;

	private final String value;

	AuthProvider(String value) {
		this.value = value;
	}

	private static Map<String, AuthProvider> map = new HashMap<>();

	static {
		for (AuthProvider provider : AuthProvider.values()) {
			map.put(provider.value, provider);
		}
	}

	public static AuthProvider fromValue(String value) {
		return map.getOrDefault(value.toLowerCase(), LOCAL);
	}
}
