package com.myproject.springbootpayment.abstracts.core;

import com.myproject.springbootpayment.shared.enums.CryptoAlgorithm;
import com.myproject.springbootpayment.utils.CryptoUtils;
import com.myproject.springbootpayment.utils.EncodingUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;

/**
 * @author nguyenle
 * @since 3:15 PM Tue 4/22/2025
 */
public abstract class AbstractCryptoService {

	protected final Charset UTF_8 = StandardCharsets.UTF_8;

	protected final Map<Integer, Map<CryptoAlgorithm, Mac>> HMAC_ENCODER = new HashMap<>();

	protected final List<String> secretKeys = new ArrayList<>();

	public abstract void initialize();

	protected void initMac(String... keys) {
		List<CryptoAlgorithm> supportedAlgorithms = CryptoAlgorithm.getSupportedAlgorithms();
		for (int i = 0; i < keys.length; i++) {
			Map<CryptoAlgorithm, Mac> map = new HashMap<>();
			String key = keys[i];
			for (CryptoAlgorithm algorithm : supportedAlgorithms) {
				try {
					Mac mac = CryptoUtils.initMac(algorithm.getAlgorithmName(), key);
					map.put(algorithm, mac);
				} catch (Exception e) {
					continue;
				}
			}
			HMAC_ENCODER.put(i + 1, map);
			secretKeys.add(key);
		}
	}

	protected Mac getHMACEncoder(String algorithmName, int useCase) {
		CryptoAlgorithm algorithm = CryptoAlgorithm.fromValue(algorithmName);
		return getHMACEncoder(algorithm, useCase);
	}

	protected Mac getHMACEncoder(CryptoAlgorithm algorithm, int useCase) {
		return HMAC_ENCODER.get(useCase).get(algorithm);
	}

	protected byte[] HMACEncode(Mac macGenerator, String data) {
		if (macGenerator == null) {
			return null;
		}
		byte[] dataByte = null;
		dataByte = data.getBytes(UTF_8);
		return macGenerator.doFinal(dataByte);
	}

	public byte[] HMACEncode(String algorithmName, String data, int useCase) {
		Mac mac = getHMACEncoder(algorithmName, useCase);
		return HMACEncode(mac, data);
	}

	public byte[] HMACEncode(CryptoAlgorithm algorithm, String data, int useCase) {
		Mac mac = getHMACEncoder(algorithm, useCase);
		return HMACEncode(mac, data);
	}

	public String HMACHexStringEncode(String algorithmName, String data, int useCase) {
		CryptoAlgorithm algorithm = CryptoAlgorithm.fromValue(algorithmName);
		return HMACHexStringEncode(algorithm, data, useCase);
	}

	public String HMACHexStringEncode(CryptoAlgorithm algorithm, String data, int useCase) {
		byte[] bytes = HMACEncode(algorithm, data, useCase);
		if (bytes == null) {
			return null;
		}
		return EncodingUtils.byteArrayToHexString(bytes);
	}

	public String HMACBase64Encode(String algorithmName, String data, int useCase) {
		CryptoAlgorithm algorithm = CryptoAlgorithm.fromValue(algorithmName);
		return HMACBase64Encode(algorithm, data, useCase);
	}

	public String HMACBase64Encode(CryptoAlgorithm algorithm, String data, int useCase) {
		byte[] bytes = HMACEncode(algorithm, data, useCase);
		if (bytes == null) {
			return null;
		}
		return EncodingUtils.base64Encode(bytes);
	}

	public String getKey(int index) {
		if (index <= 0 || index > secretKeys.size()) {
			return null;
		}
		return secretKeys.get(index - 1);
	}
}
