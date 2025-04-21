package com.myproject.springbootzalopay.shared.enums;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * @author nguyenle
 * @since 9:11 AM Mon 4/21/2025
 */
@Getter
public enum ZaloPayReturnCode {
	SUCCESS(1),
	FAIL(2),
	PROCESSING(3)
	;

	private static final Map<Integer, ZaloPayReturnCode> map = new HashMap<>();

	static {
		for (ZaloPayReturnCode code : ZaloPayReturnCode.values()) {
			map.put(code.getCode(), code);
		}
	}

	public static ZaloPayReturnCode fromValue(int value) {
		return map.get(value);
	}

	private int code;
	ZaloPayReturnCode(int code) {
		this.code = code;
	}
}
