package com.myproject.springbootvnpay.utils;

import lombok.experimental.UtilityClass;

/**
 * @author nguyenle
 * @since 11:05 AM Mon 3/10/2025
 */
@UtilityClass
public class EncodingUtils {

	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

}
