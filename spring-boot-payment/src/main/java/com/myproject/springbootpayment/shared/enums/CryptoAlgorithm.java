package com.myproject.springbootpayment.shared.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * @author nguyenle
 * @since 3:12 PM Tue 4/22/2025
 */
@Getter
public enum CryptoAlgorithm {
	HMAC_SHA512("HmacSHA512"),
	HMAC_SHA384("HmacSHA384"),
	HMAC_SHA256("HmacSHA256"),
	HMAC_SHA1("HmacSHA1"),
	HMAC_MD5("HmacMD5")
	;

	private static final Map<String, CryptoAlgorithm> map = new HashMap<>();

	static {
		for (CryptoAlgorithm algorithm : CryptoAlgorithm.values()) {
			map.put(algorithm.getAlgorithmName(), algorithm);
		}
	}

	public static List<CryptoAlgorithm> getSupportedAlgorithms() {
		return new ArrayList<>(map.values());
	}

	public static CryptoAlgorithm fromValue(String algorithm) {
		return map.get(algorithm);
	}

	private String algorithmName;
	CryptoAlgorithm(String algorithmName) {
		this.algorithmName = algorithmName;
	}

}
