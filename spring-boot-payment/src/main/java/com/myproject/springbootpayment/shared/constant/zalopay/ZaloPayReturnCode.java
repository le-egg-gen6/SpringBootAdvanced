package com.myproject.springbootpayment.shared.constant.zalopay;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nguyenle
 * @since 9:11 AM Mon 4/21/2025
 */
@Getter
public enum ZaloPayReturnCode {
	SUCCESS(1),
	FAIL(2),
	PROCESSING(3),
	UNKNOWN(-1)
	;

	private static final Map<Integer, ZaloPayReturnCode> map = new HashMap<>();

	static {
		for (ZaloPayReturnCode code : ZaloPayReturnCode.values()) {
			map.put(code.getCode(), code);
		}
	}

	public static ZaloPayReturnCode fromValue(int value) {
		return map.getOrDefault(value, UNKNOWN);
	}

	private int code;
	ZaloPayReturnCode(int code) {
		this.code = code;
	}
}
